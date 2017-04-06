package monitorx.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.Metric;
import monitorx.domain.Node;
import monitorx.domain.NodeStatus;
import monitorx.domain.syncType.SpringBootSyncTypeConfig;
import monitorx.domain.syncType.SyncTypeEnum;
import monitorx.service.NodeService;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Component
public class SpringBootNodeStatusSchedule implements SchedulingConfigurer {

    @Autowired
    NodeService nodeService;

    Logger logger = LoggerFactory.getLogger(getClass());

    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        for (Node node : nodeService.getNodes()) {
            if (node.getSyncTypeEnum() == SyncTypeEnum.SPRING_BOOT) {
                SpringBootSyncTypeConfig config = ((SpringBootSyncTypeConfig) node.getSyncTypeConfig());
                if (config != null) {
                    taskRegistrar.addFixedRateTask(new SpringBootNodeStatusTask(node), config.getInterval() * 1000);
                }
            }
        }
    }

    private class SpringBootNodeStatusTask implements Runnable {

        Node node;

        public SpringBootNodeStatusTask(Node node) {
            this.node = node;
        }

        public void run() {
            try {
                logger.info("Sync Node Status: " + node.getTitle());
                SpringBootSyncTypeConfig config = ((SpringBootSyncTypeConfig) node.getSyncTypeConfig());


                NodeStatus nodeStatus = new NodeStatus();
                nodeStatus.setMetrics(new ArrayList<Metric>());
                nodeStatus.setLastUpdateDate(new Date());

                //up status
                String healthUrl = config.getUrl() + "/health";
                String response = Request.Get(healthUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
                logger.info("Spring boot actuator health: " + healthUrl + " => " + response);
                JSONObject healthJson = JSON.parseObject(response);
                boolean up = "UP".equals(healthJson.getString("status").toUpperCase());
                nodeStatus.setStatus(up ? "up" : "down");

                //mem metric
                String metricsUrl = config.getUrl() + "/metrics";
                response = Request.Get(metricsUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
                logger.info("Spring boot actuator metrics: " + metricsUrl + " => " + response);
                JSONObject metricsJson = JSON.parseObject(response);
                Metric memoryMetric = new Metric();
                memoryMetric.setTitle("Memory(MB)");
                memoryMetric.setType("number");
                memoryMetric.setValue(String.valueOf(Math.ceil(metricsJson.getLong("mem.free") / 1024)));
                nodeStatus.getMetrics().add(memoryMetric);

                //info metric
                String infoUrl = config.getUrl() + "/info";
                response = Request.Get(infoUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
                logger.info("Spring boot actuator info: " + infoUrl + " => " + response);
                JSONObject infoJson = JSON.parseObject(response);
                Metric infoMetric = new Metric();
                infoMetric.setTitle("Info");
                infoMetric.setType("text");
                String info = JSON.toJSONString(infoJson, true);
                infoMetric.setValue(info.replace("\n", "<br/>").replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;"));
                nodeStatus.getMetrics().add(infoMetric);

                node.setStatus(nodeStatus);
                node.getStatusHistory().add(nodeStatus);

                nodeService.addCheckPoints(node);
            } catch (IOException e) {
                logger.error("Sync Node failed:" + node.getTitle(), e);
                NodeStatus nodeStatus = new NodeStatus();
                nodeStatus.setStatus("down");
                nodeStatus.setLastUpdateDate(new Date());
                node.setStatus(nodeStatus);
            }
        }
    }
}