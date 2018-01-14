package monitorx.plugins.sync.push;

import monitorx.plugins.Status;
import monitorx.plugins.sync.ISync;
import monitorx.plugins.sync.ISyncConfig;
import org.pf4j.Extension;

@Extension
public class PushSync implements ISync {

    @Override
    public String getCode() {
        return "sync-push";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public Status sync(ISyncConfig config) {
        return null;
    }

    @Override
    public ISyncConfig getSyncConfig() {
        return new PushSyncConfig();
    }
}
