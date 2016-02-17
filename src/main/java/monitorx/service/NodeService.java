package monitorx.service;

import com.alibaba.fastjson.JSON;
import monitorx.domain.Node;
import monitorx.domain.NodeStatus;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
public class NodeService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ConfigService configService;

    public List<Node> getNodes() {
        return configService.getConfig().getNodes();
    }

    public void addNode(Node node) {
        configService.getConfig().getNodes().add(node);
        configService.save();
    }

    public Node getNode(String nodeCode) {
        for (Node node : getNodes()) {
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