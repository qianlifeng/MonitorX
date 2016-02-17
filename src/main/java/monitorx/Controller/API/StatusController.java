package monitorx.Controller.API;

import com.alibaba.fastjson.JSON;
import monitorx.Domain.Node;
import monitorx.Domain.NodeStatus;
import monitorx.Domain.NodeStatusUpload;
import monitorx.Service.NodeService;
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
     *
     * @return {"success": true}
     */
    @RequestMapping(value = "/upload/", method = RequestMethod.POST)
    public String uploadStatus(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "utf-8");
        String requestBody = writer.toString();

        NodeStatusUpload nodeStatusUpload;
        try {
            nodeStatusUpload = JSON.parseObject(requestBody, NodeStatusUpload.class);
        } catch (Exception e) {
            return "{\"success\":false,\"message\":\"Invalid json format\"}";
        }

        Node node = nodeService.getNode(nodeStatusUpload.getNodeCode());
        if (node != null) {
            NodeStatus nodeStatus = nodeStatusUpload.getNodeStatus();
            nodeStatus.setLastUpdateDate(new Date());
            node.setStatus(nodeStatus);
        } else {
            return "{\"success\":false,\"message\":\"Could not find node\"}";
        }

        return "{\"success\":true}";
    }
}
