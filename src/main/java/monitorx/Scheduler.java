package monitorx;

import monitorx.Domain.Node;
import monitorx.Service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class Scheduler implements SchedulingConfigurer {

    @Autowired
    NodeService nodeService;

    Logger logger = LoggerFactory.getLogger(getClass());

    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        for (Node node : nodeService.getNodes()) {
            if (node.getSyncType().equals("pull")) {
                taskRegistrar.addFixedRateTask(new SyncNodeStatus(node), node.getRefreshInterval());
            }
        }

        //check if node is still push its status to MonitorX
        taskRegistrar.addFixedRateTask(new CheckNodeLastUpdate(nodeService.getNodes()), 1000 * 20);
    }

    private final class SyncNodeStatus implements Runnable {
        Node node;

        public SyncNodeStatus(Node node) {
            this.node = node;
        }

        public void run() {
            nodeService.syncStatus(node);
        }
    }

    private final class CheckNodeLastUpdate implements Runnable {
        List<Node> nodes;

        public CheckNodeLastUpdate(List<Node> nodes) {
            this.nodes = nodes;
        }

        public void run() {
            for (Node node : nodeService.getNodes()) {
                if (node.getSyncType().equals("push") && node.getStatus() != null && node.getStatus().getLastUpdateDate() != null) {
                    long seconds = (new Date().getTime() - node.getStatus().getLastUpdateDate().getTime()) / 1000;
                    if (seconds > 30) {
                        node.getStatus().setStatus("down");
                        node.getStatus().setLastUpdateDate(new Date());
                        logger.warn("Node is down:" + node.getCode());
                    }
                }
            }
        }
    }
}