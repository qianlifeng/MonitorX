package monitorx;

import monitorx.Domain.Node;
import monitorx.Service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

@Component
public class Scheduler implements SchedulingConfigurer {

    @Autowired
    NodeService nodeService;

    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        for (Node node : nodeService.getNodes()) {
            taskRegistrar.addFixedRateTask(new SyncNodeStatus(node), node.getRefreshInterval());
        }
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
}