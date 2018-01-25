package monitorx.controller.api;

import monitorx.domain.Node;
import monitorx.plugins.Status;
import monitorx.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeService nodeService;

    @RequestMapping(value = "/{node}/", method = RequestMethod.GET)
    public String queryStatus(@PathVariable("node") String nodeCode) {
        String status = "down";

        Node node = nodeService.getNode(nodeCode);
        if (node == null) {
            return "Invalid node";
        }

        Status nodeStatus = node.getStatus();
        if (nodeStatus != null) {
            status = nodeStatus.getStatus();
        }
        return status;
    }
}
