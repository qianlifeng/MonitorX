package monitorx.domain;

import monitorx.plugins.Status;

public class NodeStatusUpload {
    String nodeCode;
    Status nodeStatus;

    public Status getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(Status nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }
}