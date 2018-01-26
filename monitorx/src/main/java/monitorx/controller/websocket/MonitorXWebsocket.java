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
        this.simpMessagingTemplate.convertAndSend("/topic/sync/nodes", JSON.toJSONString(nodeService.getNodes()));
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