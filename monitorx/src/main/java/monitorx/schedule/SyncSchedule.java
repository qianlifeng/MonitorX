package monitorx.schedule;

import monitorx.plugins.sync.ISync;
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
            taskRegistrar.addFixedRateTask(new SyncTask(syncPlugin), 5000);
        }
    }

    private class SyncTask implements Runnable {
        ISync sync;

        SyncTask(ISync sync) {
            this.sync = sync;
        }

        @Override
        public void run() {
            nodeService.getNodes().stream().filter(o -> o.getSyncCode().equals(sync.getCode())).forEach(node -> {
                try {
                    sync.sync(node.getSyncConfig());
                } catch (Exception e) {
                    logger.error("sync task ({}) failed", node.getTitle(), e);
                }
            });
        }
    }
}