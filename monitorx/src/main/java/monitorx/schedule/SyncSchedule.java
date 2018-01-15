package monitorx.schedule;

import monitorx.plugins.Status;
import monitorx.plugins.sync.ISync;
import monitorx.plugins.sync.SyncContext;
import monitorx.service.NodeService;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyncSchedule implements SchedulingConfigurer {

    @Autowired
    NodeService nodeService;

    @Autowired
    PluginManager pluginManager;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        List<ISync> syncPlugins = pluginManager.getExtensions(ISync.class);
        for (ISync syncPlugin : syncPlugins) {
            taskRegistrar.addFixedDelayTask(new SyncTask(syncPlugin), 5000);
        }
    }

    private class SyncTask implements Runnable {
        ISync sync;

        SyncTask(ISync sync) {
            this.sync = sync;
        }

        @Override
        public void run() {
            nodeService.getNodes().stream().filter(o -> o.getSync().equals(sync.getCode())).forEach(node -> {
                try {
                    SyncContext syncContext = new SyncContext();
                    syncContext.setNodeCode(node.getCode());
                    syncContext.setSyncConfig(node.getSyncConfig());
                    Status status = this.sync.sync(syncContext);
                    if (status != null) {
                        if ("down".equals(status.getStatus())) {
                            logger.warn("node {} is down", node.getCode());
                        }
                        node.setStatus(status);
                        node.getStatusHistory().add(status);
                        nodeService.addCheckPoints(node);
                    }
                } catch (Exception e) {
                    logger.error("sync task ({}) failed", node.getTitle(), e);
                }
            });
        }
    }
}