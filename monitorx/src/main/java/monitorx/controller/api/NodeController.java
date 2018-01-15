package monitorx.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import monitorx.plugins.Metric;
import monitorx.domain.Node;
import monitorx.domain.forewarning.Forewarning;
import monitorx.plugins.sync.ISyncConfig;
import monitorx.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    @Autowired
    NodeService nodeService;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public APIResponse getNodeList() {
        return APIResponse.buildSuccessResponse(nodeService.getNodes());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public APIResponse addNode(HttpServletRequest request) {
        String nodeJSON = request.getParameter("node");

        Node node = JSON.parseObject(nodeJSON, Node.class);

        JSONObject jsonObject = JSON.parseObject(nodeJSON);
        ISyncConfig config = ((ISyncConfig) applicationContext.getBean("syncTypeConfig-" + node.getSyncCode()));

        ISyncConfig nodeConfig = JSON.parseObject(JSON.toJSONString(jsonObject.get("syncTypeConfig")), config.getClass());
        node.setSyncConfig(nodeConfig);

        nodeService.addNode(node);
        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/{nodeCode}/", method = RequestMethod.GET)
    public APIResponse getNode(@PathVariable("nodeCode") String nodeCode) {
        Node node = nodeService.getNode(nodeCode);
        if (node.getStatus() != null && node.getStatus().getMetrics() != null) {
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
        return APIResponse.buildSuccessResponse(node);
    }


    @RequestMapping(value = "/{nodeCode}/{forewarning}/checkpoint/", method = RequestMethod.GET)
    public APIResponse getNodeCheckPoints(@PathVariable("nodeCode") String nodeCode, @PathVariable("forewarning") String forewarning) {
        Forewarning fw = nodeService.findForewarningByTitle(nodeCode, forewarning);
        return APIResponse.buildSuccessResponse(fw.getFireRuleContext().getCheckPoints());
    }

    @RequestMapping(value = "/{nodeCode}/", method = RequestMethod.DELETE)
    public APIResponse removeNode(@PathVariable("nodeCode") String nodeCode) {
        nodeService.removeNode(nodeCode);
        return APIResponse.buildSuccessResponse();
    }
}