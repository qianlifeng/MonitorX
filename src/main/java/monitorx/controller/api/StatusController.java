package monitorx.controller.api;

import com.alibaba.fastjson.JSON;
import monitorx.domain.Node;
import monitorx.domain.NodeStatus;
import monitorx.domain.NodeStatusUpload;
import monitorx.service.NodeService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    NodeService nodeService;

    /**
     * App upload status to MonitorX
     */
    @RequestMapping(value = "/upload/", method = RequestMethod.POST)
    public APIResponse uploadStatus(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "utf-8");
        String requestBody = writer.toString();

        NodeStatusUpload nodeStatusUpload;
        try {
            nodeStatusUpload = JSON.parseObject(requestBody, NodeStatusUpload.class);
        } catch (Exception e) {
            return APIResponse.buildErrorResponse("Invalid json format");
        }

        Node node = nodeService.getNode(nodeStatusUpload.getNodeCode());
        if (node != null) {
            NodeStatus nodeStatus = nodeStatusUpload.getNodeStatus();
            nodeStatus.setLastUpdateDate(new Date());
            node.setStatus(nodeStatus);

            nodeService.addCheckPoints(node);
            nodeService.checkForewarningAndNotify(node);
        } else {
            return APIResponse.buildErrorResponse("Could not find node");
        }

        return APIResponse.buildSuccessResponse();
    }
}
