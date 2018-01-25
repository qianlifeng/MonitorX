package monitorx.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.Notifier;
import monitorx.plugins.annotation.UIField;
import monitorx.plugins.notifier.INotifier;
import monitorx.plugins.notifier.INotifierConfig;
import monitorx.plugins.notifier.NotifierContext;
import monitorx.service.NotifierService;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifier")
public class NotifierController {

    @Autowired
    NotifierService notifierService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PluginManager pluginManager;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public APIResponse getNotifierList() {
        return APIResponse.buildSuccessResponse(notifierService.getNotifiers());
    }

    @RequestMapping(value = "/codes/", method = RequestMethod.GET)
    public APIResponse getNotifierCodes() {
        List<JSONObject> notifiers = pluginManager.getExtensions(INotifier.class).stream().map(o -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", o.getCode());
            jsonObject.put("name", o.getName());
            jsonObject.put("description", o.getDescription());
            jsonObject.put("icon", o.getFontAwesomeIcon());
            JSONArray configs = new JSONArray();
            for (Field configField : o.getNotifierConfig().getClass().getDeclaredFields()) {
                JSONObject field = new JSONObject();
                UIField uiField = configField.getAnnotation(UIField.class);
                field.put("code", uiField.code());
                field.put("name", uiField.name());
                field.put("type", uiField.type());
                field.put("description", uiField.description());
                configs.add(field);
            }
            jsonObject.put("config", configs);
            return jsonObject;
        }).collect(Collectors.toList());
        return APIResponse.buildSuccessResponse(notifiers);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public APIResponse addNotifier(HttpServletRequest request) {
        Notifier notifier = buildNotifier(request);
        notifierService.addNotifier(notifier);
        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/testsend/", method = RequestMethod.POST)
    public APIResponse testSend(HttpServletRequest request) {
        String title = request.getParameter("title");
        String msg = request.getParameter("body");

        Notifier notifier = buildNotifier(request);
        pluginManager.getExtensions(INotifier.class).stream().filter(o -> o.getCode().equals(notifier.getNotifierCode())).findFirst().ifPresent(n -> {
            NotifierContext context = new NotifierContext();
            context.setTitle(title);
            context.setContent(msg);
            context.setNotifierConfig(notifier.getNotifierConfig());
            n.send(context);
        });
        return APIResponse.buildSuccessResponse();
    }

    private Notifier buildNotifier(HttpServletRequest request) {
        String notifierJSON = request.getParameter("notifier");

        Notifier notifier = JSON.parseObject(notifierJSON, Notifier.class);
        notifier.setId(UUID.randomUUID().toString());

        JSONObject jsonObject = JSON.parseObject(notifierJSON);
        pluginManager.getExtensions(INotifier.class).stream().filter(o -> o.getCode().equals(notifier.getNotifierCode())).findFirst().ifPresent(n -> {
            INotifierConfig configClass = n.getNotifierConfig();
            INotifierConfig notifierConfig = jsonObject.getObject("notifierConfig", configClass.getClass());
            notifier.setNotifierConfig(notifierConfig);
            notifier.setFontawesomeIcon(n.getFontAwesomeIcon());
        });

        return notifier;
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.GET)
    public APIResponse getNotifier(@PathVariable("id") String id) {
        return APIResponse.buildSuccessResponse(notifierService.getNotifier(id));
    }

    @RequestMapping(value = "/{id}/", method = RequestMethod.DELETE)
    public APIResponse removeNotifier(@PathVariable("id") String id) {
        notifierService.removeNotifier(id);
        return APIResponse.buildSuccessResponse();
    }
}