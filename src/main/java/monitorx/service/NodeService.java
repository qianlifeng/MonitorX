package monitorx.service;

import monitorx.domain.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void removeNode(String nodeCode) {
        Node node = getNode(nodeCode);
        if (node != null) {
            getNodes().remove(node);
            configService.save();
        }
    }
}