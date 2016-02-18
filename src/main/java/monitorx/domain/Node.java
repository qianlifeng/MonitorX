package monitorx.domain;

import com.alibaba.fastjson.annotation.JSONField;
import monitorx.domain.forewarning.Forewarning;
import monitorx.domain.syncType.ISyncTypeConfig;
import monitorx.domain.syncType.SyncTypeEnum;

import java.util.List;

/**
 * Node that need to be monitored
 */
public class Node {
    String code;
    String title;

    /**
     * @see SyncTypeEnum
     */
    String syncType;

    ISyncTypeConfig syncTypeConfig;

    NodeStatus status;

    List<Forewarning> forewarnings;

    public String getSyncType() {
        return syncType;
    }

    @JSONField(serialize = false)
    public SyncTypeEnum getSyncTypeEnum() {
        return SyncTypeEnum.getByCode(syncType);
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

    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Node)) return false;
        Node otherNode = (Node) other;
        return this.code.equals(otherNode.getCode());
    }
}