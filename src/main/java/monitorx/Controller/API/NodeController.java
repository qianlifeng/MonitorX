package monitorx.controller.API;

import monitorx.domain.Node;
import monitorx.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    @Autowired
    NodeService nodeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Node> getNodeList() {
        return nodeService.getNodes();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void addNode(Node node) {
        nodeService.addNode(node);
    }

    @RequestMapping(value = "/{nodeCode}/", method = RequestMethod.GET)
    public Node getNodeList(@PathVariable("nodeCode") String nodeCode) {
        return nodeService.getNode(nodeCode);
    }
}
