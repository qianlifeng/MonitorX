package monitorx.Service;

import com.alibaba.fastjson.JSON;
import monitorx.Domain.Node;
import monitorx.Domain.NodeStatus;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NodeService {

    Logger logger = LoggerFactory.getLogger(getClass());

    List<Node> nodes = new ArrayList<Node>();

    public List<Node> getNodes() {
        if (nodes == null || nodes.size() == 0) {
            //Mock
            Node oms = new Node();
            oms.setTitle("回写");
            oms.setCode("callback");
            oms.setSyncType("push");
            nodes.add(oms);

            Node netease = new Node();
            netease.setTitle("网易接单");
            netease.setCode("oms-netease");
            netease.setSyncType("push");
            nodes.add(netease);
        }
        return nodes;
    }

    public Node getNode(String nodeCode) {
        for (Node node : nodes) {
            if (node.getCode().equals(nodeCode)) {
                return node;
            }
        }

        return null;
    }

    public void syncStatus(Node node) {
        logger.info("Sync Node Status: " + node.getTitle());
        try {
            String response = Request.Get(node.getUrl()).execute().returnContent().asString(StandardCharsets.UTF_8);
            NodeStatus nodeStatus = JSON.parseObject(response, NodeStatus.class);
            nodeStatus.setLastUpdateDate(new Date());
            node.setStatus(nodeStatus);
        } catch (IOException e) {
            NodeStatus nodeStatus = new NodeStatus();
            nodeStatus.setStatus("down");
            nodeStatus.setLastUpdateDate(new Date());
            node.setStatus(nodeStatus);
            logger.error("Sync Node failed:" + node.getTitle(), e);
        }
    }
}