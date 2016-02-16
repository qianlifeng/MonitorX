package monitorx.Controller;

import monitorx.Domain.Node;
import monitorx.Service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    NodeService nodeService;

    @RequestMapping("/")
    public List<Node> getNodeList() {
        return nodeService.getNodes();
    }

    @RequestMapping("/{nodeCode}/")
    public Node getNodeList(@PathVariable("nodeCode") String nodeCode) {
        return nodeService.getNode(nodeCode);
    }
}
