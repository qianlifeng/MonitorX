package monitorx.controller.api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import monitorx.plugins.annotation.UIField;
import monitorx.plugins.sync.ISync;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qianlifeng
 */
@RestController
@RequestMapping("/api/sync")
public class SyncController {

    @Autowired
    PluginManager pluginManager;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public APIResponse getSyncList() {
        List<JSONObject> syncs = pluginManager.getExtensions(ISync.class).stream().map(o -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", o.getCode());
            jsonObject.put("name", o.getName());
            jsonObject.put("description", o.getDescription());
            JSONArray configs = new JSONArray();
            for (Field configField : o.getSyncConfig().getClass().getDeclaredFields()) {
                JSONObject field = new JSONObject();
                UIField syncConfig = configField.getAnnotation(UIField.class);
                field.put("code", syncConfig.code());
                field.put("name", syncConfig.name());
                field.put("type", syncConfig.type());
                field.put("description", syncConfig.description());
                configs.add(field);
            }
            jsonObject.put("config", configs);
            return jsonObject;
        }).collect(Collectors.toList());
        return APIResponse.buildSuccessResponse(syncs);
    }
}
