package monitorx.domain;

import com.alibaba.fastjson.annotation.JSONField;
import monitorx.domain.forewarning.Forewarning;
import monitorx.plugins.Status;
import monitorx.plugins.sync.ISyncConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Node that need to be monitored
 */
public class Node {
    String code;
    String title;
    String sync;
    ISyncConfig syncConfig;

    @JSONField(serialize = false)
    Status status;

    @JSONField(serialize = false)
    List<Status> statusHistory = new ArrayList<Status>();

    List<Forewarning> forewarnings = new ArrayList<Forewarning>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public ISyncConfig getSyncConfig() {
        return syncConfig;
    }

    public void setSyncConfig(ISyncConfig syncConfig) {
        this.syncConfig = syncConfig;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Status> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<Status> statusHistory) {
        this.statusHistory = statusHistory;
    }

    public List<Forewarning> getForewarnings() {
        return forewarnings;
    }

    public void setForewarnings(List<Forewarning> forewarnings) {
        this.forewarnings = forewarnings;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Node)) {
            return false;
        }
        Node otherNode = (Node) other;
        return this.code.equals(otherNode.getCode());
    }
}