package monitorx.controller.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.Node;
import monitorx.plugins.Metric;
import monitorx.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qianlifeng
 */
@Controller
public class MonitorXWebsocket {

    @Autowired
    NodeService nodeService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void pushAllNodesToClient() {
        List<JSONObject> nodes = nodeService.getNodes().stream().map(o -> {
            JSONObject node = new JSONObject();
            node.put("group", o.getGroup());
            node.put("title", o.getTitle());
            node.put("code", o.getCode());

            if (o.getStatus() != null) {
                JSONObject status = new JSONObject();
                status.put("status", o.getStatus().getStatus());
                status.put("formattedLastUpdateDate", o.getStatus().getFormattedLastUpdateDate());
                node.put("status", status);
            }

            return node;
        }).collect(Collectors.toList());
        this.simpMessagingTemplate.convertAndSend("/topic/sync/nodes", JSON.toJSONString(nodes));
    }

    public void pushNodeStatusToClient(String nodeCode) {
        Node node = nodeService.getNode(nodeCode);
        if (node != null && node.getStatus() != null && node.getStatus() != null && node.getStatus().getMetrics() != null) {
            for (Metric metric : node.getStatus().getMetrics()) {
                if (metric.isLineMetric()) {
                    JSONObject lineValue = JSON.parseObject(metric.getValue());
                    int lastMax = 10; //default keep 10 value history
                    if (lineValue.get("xcount") != null) {
                        lastMax = lineValue.getInteger("xcount");
                    }

                    int interval = 60; //seconds
                    if (lineValue.get("xinterval") != null) {
                        interval = lineValue.getInteger("xinterval");
                    }
                    metric.setHistoryValue(nodeService.getLastMetricValueHistoryByTimeInterval(metric, node.getStatusHistory(), lastMax, interval));
                }
            }
        }
        this.simpMessagingTemplate.convertAndSend("/topic/sync/node/" + nodeCode, JSON.toJSONString(node));
    }

    @MessageMapping("/sync/nodes")
    public void syncNodes() {
        pushAllNodesToClient();
    }

    @MessageMapping("/sync/node/{code}")
    public void syncNodes(@DestinationVariable String code) {
        pushNodeStatusToClient(code);
    }
}