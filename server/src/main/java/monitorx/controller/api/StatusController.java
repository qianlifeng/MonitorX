package monitorx.controller.api;

import com.alibaba.fastjson.JSON;
import monitorx.domain.Node;
import monitorx.domain.NodeStatus;
import monitorx.domain.NodeStatusUpload;
import monitorx.service.NodeService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NodeService nodeService;

    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

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
            logger.error("Invalid json format:" + requestBody);
            return APIResponse.buildErrorResponse("Invalid json format");
        }

        logger.info("Upload status from ip:" + getIpAddr(request) + ", request body:" + requestBody);
        Node node = nodeService.getNode(nodeStatusUpload.getNodeCode());
        if (node != null) {
            NodeStatus nodeStatus = nodeStatusUpload.getNodeStatus();
            nodeStatus.setLastUpdateDate(new Date());
            node.setStatus(nodeStatus);
            node.getStatusHistory().add(nodeStatus);

            nodeService.addCheckPoints(node);
        } else {
            return APIResponse.buildErrorResponse("Could not find node");
        }

        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/{node}/", method = RequestMethod.GET)
    public String queryStatus(@PathVariable("node") String nodeCode) throws IOException {
        String status = "down";

        Node node = nodeService.getNode(nodeCode);
        if (node == null) return "Invalid node";

        NodeStatus nodeStatus = node.getStatus();
        if (nodeStatus != null) {
            status = nodeStatus.getStatus();
        }
        return status;
    }
}
