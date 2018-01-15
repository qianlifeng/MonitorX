package monitorx.controller.api;

import com.alibaba.fastjson.JSONObject;
import monitorx.plugins.sync.ISync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qianlifeng
 */
@RestController
@RequestMapping("/api/sync")
public class SyncController {

    @Autowired
    List<ISync> syncList;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public APIResponse getNodeList() {
        List<JSONObject> syncs = syncList.stream().map(o -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", o.getCode());
            jsonObject.put("name", o.getName());
            jsonObject.put("description", o.getDescription());
            return jsonObject;
        }).collect(Collectors.toList());
        return APIResponse.buildSuccessResponse(syncs);
    }
}
