package monitorx.schedule;

import com.alibaba.fastjson.JSON;
import monitorx.domain.Node;
import monitorx.domain.NodeStatus;
import monitorx.domain.syncType.PullSyncTypeConfig;
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
import java.util.Date;

@Component
public class PullNodeStatusSchedule implements SchedulingConfigurer {

    @Autowired
    NodeService nodeService;

    @Autowired
    PushNodeStatusSchedule pushNodeStatusTask;

    Logger logger = LoggerFactory.getLogger(getClass());

    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        for (Node node : nodeService.getNodes()) {
            if (node.getSyncTypeEnum() == SyncTypeEnum.PULL) {
                PullSyncTypeConfig config = ((PullSyncTypeConfig) node.getSyncTypeConfig());
                taskRegistrar.addFixedRateTask(new PullNodeStatusTask(node), config.getInterval() * 1000);
            }
        }
    }

    private class PullNodeStatusTask implements Runnable {

        Node node;

        public PullNodeStatusTask(Node node) {
            this.node = node;
        }

        public void run() {
            try {
                logger.info("Sync Node Status: " + node.getTitle());
                PullSyncTypeConfig config = ((PullSyncTypeConfig) node.getSyncTypeConfig());
                String response = Request.Get(config.getUrl()).execute().returnContent().asString(StandardCharsets.UTF_8);
                NodeStatus nodeStatus = JSON.parseObject(response, NodeStatus.class);
                nodeStatus.setLastUpdateDate(new Date());
                node.setStatus(nodeStatus);
            } catch (IOException e) {
                NodeStatus nodeStatus = new NodeStatus();
                nodeStatus.setStatus("down");
                nodeStatus.setLastUpdateDate(new Date());
                node.setStatus(nodeStatus);
                logger.error("Sync Node failed:" + node.getTitle(), e);
            }
        }
    }
}