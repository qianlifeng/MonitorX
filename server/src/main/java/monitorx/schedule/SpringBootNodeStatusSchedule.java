package monitorx.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.Metric;
import monitorx.domain.Node;
import monitorx.domain.NodeStatus;
import monitorx.domain.syncType.SpringBootSyncTypeConfig;
import monitorx.domain.syncType.SyncTypeEnum;
import monitorx.service.NodeService;
import org.apache.commons.lang3.time.DateFormatUtils;
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

                //health status
                Metric healthMetric = new Metric();
                healthMetric.setTitle("Health");
                healthMetric.setType("text");
                StringBuilder sb = new StringBuilder();
                JSONObject jms = healthJson.getJSONObject("jms");
                if (jms != null) {
                    boolean isUp = "UP".equals(jms.getString("status"));
                    sb.append("<div class='alert alert-" + (isUp ? "success" : "danger") + "'><i class='fa fa-paper-plane-o'></i> JMS - " + jms.getString("provider")
                            + "<span class='pull-right'>" + jms.getString("status") + "</span></div>");
                }
                JSONObject disk = healthJson.getJSONObject("diskSpace");
                if (disk != null) {
                    boolean isUp = "UP".equals(disk.getString("status"));
                    long total = Math.round(disk.getLong("total") / (1024 * 1024 * 1024));
                    long free = Math.round(disk.getLong("free") / (1024 * 1024 * 1024));
                    sb.append("<div class='alert alert-" + (isUp ? "success" : "danger") + "'><i class='fa fa-tasks'></i>  DISK - Total: " + total + "G, Free: " + free
                            + "G<span class='pull-right'>" + jms.getString("status") + "</span></div>");
                }
                JSONObject db = healthJson.getJSONObject("db");
                if (db != null) {
                    boolean isUp = "UP".equals(db.getString("status"));
                    sb.append("<div class='alert alert-" + (isUp ? "success" : "danger") + "'><i class='fa fa-database'></i> DB - " + db.getString("database")
                            + "<span class='pull-right'>" + jms.getString("status") + "</span></div>");
                }
                healthMetric.setValue(sb.toString());
                healthMetric.setContext(response);
                nodeStatus.getMetrics().add(healthMetric);

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
                infoMetric.setContext(info);
                nodeStatus.getMetrics().add(infoMetric);

                //mem metric
                String metricsUrl = config.getUrl() + "/metrics";
                response = Request.Get(metricsUrl).execute().returnContent().asString(StandardCharsets.UTF_8);
                logger.info("Spring boot actuator metrics: " + metricsUrl + " => " + response);
                JSONObject metricsJson = JSON.parseObject(response);
                Metric memoryMetric = new Metric();
                memoryMetric.setTitle("Used Memory(Mb)");
                memoryMetric.setType("line");
                memoryMetric.setWidth(1.0);
                long freeMemory = Math.round(metricsJson.getLong("mem.free") / 1024);
                long totalMemory = Math.round(metricsJson.getLong("mem") / 1024);
                JSONObject val = new JSONObject();
                val.put("x", DateFormatUtils.format(new Date(), "HH:mm"));
                val.put("y", totalMemory - freeMemory);
                val.put("xcount", 10);
                memoryMetric.setValue(JSON.toJSONString(val));
                nodeStatus.getMetrics().add(memoryMetric);

                Metric memoryDetailMetric = new Metric();
                memoryDetailMetric.setTitle("Memory");
                memoryDetailMetric.setType("text");
                StringBuilder memoryDetailBuilder = new StringBuilder();
                //memroy
                memoryDetailBuilder.append("<div style='margin-bottom:5px;'><b>Memory</b> ( " + (totalMemory - freeMemory) + "M / " + totalMemory + "M )</div>");
                long memroyPercent = Math.round(((totalMemory - freeMemory * 1.0) / totalMemory) * 100);
                memoryDetailBuilder.append("<div class='progress'><div class='progress-bar progress-bar-striped' style='width: " + memroyPercent + "%;'>" + memroyPercent + "%</div></div>");
                //heap memory
                memoryDetailMetric.setValue(memoryDetailBuilder.toString());
                memoryDetailMetric.setContext(response);
                nodeStatus.getMetrics().add(memoryDetailMetric);

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