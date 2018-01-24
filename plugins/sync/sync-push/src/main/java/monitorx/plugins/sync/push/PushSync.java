package monitorx.plugins.sync.push;

import monitorx.plugins.Status;
import monitorx.plugins.sync.ISync;
import monitorx.plugins.sync.ISyncConfig;
import monitorx.plugins.sync.SyncContext;
import org.pf4j.Extension;

@Extension
public class PushSync implements ISync {

    @Override
    public String getCode() {
        return "sync-push";
    }

    @Override
    public String getName() {
        return "Push";
    }

    @Override
    public String getDescription() {
        return "Push node status to <Monitorx Host>/api/status/upload/";
    }

    @Override
    public Status sync(SyncContext syncContext) {
        Status status = NodeStatus.getLatestNodeStatus(syncContext.getNodeCode(), (PushSyncConfig) syncContext.getSyncConfig());
        if (status != null) {
            return status;
        }

        return Status.down();
    }

    @Override
    public ISyncConfig getSyncConfig() {
        return new PushSyncConfig();
    }
}