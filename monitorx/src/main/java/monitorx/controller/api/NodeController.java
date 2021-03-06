package monitorx.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.Forewarning;
import monitorx.domain.Node;
import monitorx.plugins.sync.ISync;
import monitorx.plugins.sync.ISyncConfig;
import monitorx.service.NodeService;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    @Autowired
    NodeService nodeService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PluginManager pluginManager;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public APIResponse addNode(HttpServletRequest request) {
        String nodeJSON = request.getParameter("node");

        Node node = JSON.parseObject(nodeJSON, Node.class);
        List<ISync> syncs = pluginManager.getExtensions(ISync.class);
        Optional<ISync> sync = syncs.stream().filter(o -> o.getCode().equalsIgnoreCase(node.getSync())).findFirst();
        if (!sync.isPresent()) {
            return APIResponse.buildErrorResponse("Invalid Sync");
        }

        JSONObject jsonObject = JSON.parseObject(nodeJSON);
        ISyncConfig nodeConfig = JSON.parseObject(JSON.toJSONString(jsonObject.get("syncConfig")), sync.get().getSyncConfig().getClass());
        node.setSyncConfig(nodeConfig);

        nodeService.addNode(node);
        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/{nodeCode}/{forewarning}/checkpoint/", method = RequestMethod.GET)
    public APIResponse getNodeCheckPoints(@PathVariable("nodeCode") String nodeCode, @PathVariable("forewarning") String forewarning) {
        Forewarning fw = nodeService.findForewarningByTitle(nodeCode, forewarning);
        return APIResponse.buildSuccessResponse(fw.getCheckPoints());
    }

    @RequestMapping(value = "/{nodeCode}/", method = RequestMethod.DELETE)
    public APIResponse removeNode(@PathVariable("nodeCode") String nodeCode) {
        nodeService.removeNode(nodeCode);
        return APIResponse.buildSuccessResponse();
    }
}