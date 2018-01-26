package monitorx.schedule;

import monitorx.controller.websocket.MonitorXWebsocket;
import monitorx.domain.Node;
import monitorx.plugins.Status;
import monitorx.plugins.sync.ISync;
import monitorx.plugins.sync.SyncContext;
import monitorx.service.NodeService;
import monitorx.util.TimeoutThread;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author qianlifeng
 */
@Component
public class SyncSchedule {

    @Autowired
    NodeService nodeService;

    @Autowired
    PluginManager pluginManager;

    @Autowired
    MonitorXWebsocket monitorXWebsocket;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Scheduled(fixedDelay = 5000)
    public void runSchedule() {
        List<ISync> availableSyncs = pluginManager.getExtensions(ISync.class);
        nodeService.getNodes().forEach(node -> {
            availableSyncs.stream().filter(i -> node.getSync().equals(i.getCode())).findFirst().ifPresent(isync -> {
                new TimeoutThread(node, new SyncTask(node, isync), 4000).execute();
            });
        });
        monitorXWebsocket.pushAllNodesToClient();
    }

    private class SyncTask implements Runnable {
        Node node;
        ISync iSync;

        SyncTask(Node node, ISync iSync) {
            this.node = node;
            this.iSync = iSync;
        }

        @Override
        public void run() {
            SyncContext syncContext = new SyncContext();
            syncContext.setNodeCode(node.getCode());
            syncContext.setSyncConfig(node.getSyncConfig());

            try {
                Status status = iSync.sync(syncContext);
                if (status != null) {
                    updateStatus(status);
                }
            } catch (InterruptedException e) {
                //execution timeout
                logger.error("node {} executes timeout", node.getTitle(), e.getMessage());
                updateStatus(Status.down());
            } catch (Exception e) {
                updateStatus(Status.down());
            } finally {
                monitorXWebsocket.pushNodeStatusToClient(node.getCode());
            }
        }

        private void updateStatus(Status status) {
            status.setLastUpdateDate(new Date());
            if ("down".equals(status.getStatus())) {
                logger.error("node {} is down", node.getCode());
            }
            node.setStatus(status);
            node.getStatusHistory().add(status);
            nodeService.addCheckPoints(node);
        }
    }
}