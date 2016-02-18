package monitorx.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.notifier.INotifierConfig;
import monitorx.domain.notifier.Notifier;
import monitorx.service.NotifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifier")
public class NotifierController {

    @Autowired
    NotifierService notifierService;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public APIResponse getNotifierList() {
        return APIResponse.buildSuccessResponse(notifierService.getNotifiers());
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public APIResponse addNotifier(HttpServletRequest request) throws IOException {
        Notifier notifier = buildNotifier(request);
        notifierService.addNotifier(notifier);
        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/testsend/", method = RequestMethod.POST)
    public APIResponse testSend(HttpServletRequest request) throws IOException {
        String title = request.getParameter("title");
        String msg = request.getParameter("msg");

        Notifier notifier = buildNotifier(request);
        notifier.send(title, msg);
        return APIResponse.buildSuccessResponse();
    }

    private Notifier buildNotifier(HttpServletRequest request) {
        String notifierJSON = request.getParameter("notifier");

        Notifier notifier = JSON.parseObject(notifierJSON, Notifier.class);
        notifier.setId(UUID.randomUUID().toString());

        JSONObject jsonObject = JSON.parseObject(notifierJSON);
        INotifierConfig config = ((INotifierConfig) applicationContext.getBean("notifierConfig-" + notifier.getType()));

        INotifierConfig notifierConfig = JSON.parseObject(JSON.toJSONString(jsonObject.get("config")), config.getClass());
        notifier.setConfig(notifierConfig);
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