package monitorx.domain;

/**
 * Node that need to be monitored
 */
public class Node {
    String code;
    String title;
    String url;
    /**
     * pull/push
     */
    String syncType;

    /**
     * Seconds
     */
    int refreshInterval = 3 * 1000;

    NodeStatus status;

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public String toString() {
        return title;
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Node)) return false;
        Node otherNode = (Node) other;
        return this.title.equals(otherNode.getTitle());
    }
}