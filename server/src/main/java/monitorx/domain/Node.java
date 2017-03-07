package monitorx.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import monitorx.domain.forewarning.Forewarning;
import monitorx.domain.syncType.ISyncTypeConfig;
import monitorx.domain.syncType.SyncTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Node that need to be monitored
 */
public class Node {
    String code;
    String title;

    Integer checkIntervalSeconds;

    /**
     * @see SyncTypeEnum
     */
    String syncType;

    ISyncTypeConfig syncTypeConfig;

    @JSONField(serialize = false)
    NodeStatus status;

    @JSONField(serialize = false)
    @JsonIgnore
    List<NodeStatus> statusHistory = new ArrayList<NodeStatus>();

    List<Forewarning> forewarnings = new ArrayList<Forewarning>();

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }

    @JSONField(serialize = false)
    public SyncTypeEnum getSyncTypeEnum() {
        return SyncTypeEnum.getByCode(syncType);
    }

    public List<NodeStatus> getStatusHistory() {
        return statusHistory;
    }

    public void setStatusHistory(List<NodeStatus> statusHistory) {
        this.statusHistory = statusHistory;
    }

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

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public String toString() {
        return code;
    }

    public ISyncTypeConfig getSyncTypeConfig() {
        return syncTypeConfig;
    }

    public void setSyncTypeConfig(ISyncTypeConfig syncTypeConfig) {
        this.syncTypeConfig = syncTypeConfig;
    }

    public List<Forewarning> getForewarnings() {
        return forewarnings;
    }

    public void setForewarnings(List<Forewarning> forewarnings) {
        this.forewarnings = forewarnings;
    }

    public Integer getCheckIntervalSeconds() {
        return checkIntervalSeconds;
    }

    public void setCheckIntervalSeconds(Integer checkIntervalSeconds) {
        this.checkIntervalSeconds = checkIntervalSeconds;
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Node)) return false;
        Node otherNode = (Node) other;
        return this.code.equals(otherNode.getCode());
    }
}