package monitorx.plugins.sync;

/**
 * @author qianlifeng
 */
public class SyncContext {
    String nodeCode;
    ISyncConfig syncConfig;

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public ISyncConfig getSyncConfig() {
        return syncConfig;
    }

    public void setSyncConfig(ISyncConfig syncConfig) {
        this.syncConfig = syncConfig;
    }
}
