package monitorx.domain.syncType;

public class PullSyncTypeConfig implements ISyncTypeConfig {
    /**
     * Seconds
     */
    int refreshInterval = 3 * 1000;

    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }
}