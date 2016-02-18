package monitorx.controller.api;

import monitorx.domain.Node;
import monitorx.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    @Autowired
    NodeService nodeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public APIResponse getNodeList() {
        return APIResponse.buildSuccessResponse(nodeService.getNodes());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public APIResponse addNode(Node node) {
        nodeService.addNode(node);
        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/{nodeCode}/", method = RequestMethod.GET)
    public APIResponse getNode(@PathVariable("nodeCode") String nodeCode) {
        return APIResponse.buildSuccessResponse(nodeService.getNode(nodeCode));
    }

    @RequestMapping(value = "/{nodeCode}/", method = RequestMethod.DELETE)
    public APIResponse removeNode(@PathVariable("nodeCode") String nodeCode) {
        nodeService.removeNode(nodeCode);
        return APIResponse.buildSuccessResponse();
    }
}