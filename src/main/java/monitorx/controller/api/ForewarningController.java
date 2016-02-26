package monitorx.controller.api;

import monitorx.domain.Node;
import monitorx.domain.forewarning.Forewarning;
import monitorx.domain.forewarning.IFireRule;
import monitorx.domain.notifier.Notifier;
import monitorx.service.ConfigService;
import monitorx.service.JavascriptEngine;
import monitorx.service.NodeService;
import monitorx.service.NotifierService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/forewarning")
public class ForewarningController {

    @Autowired
    NotifierService notifierService;

    @Autowired
    NodeService nodeService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ConfigService configService;

    @Autowired
    JavascriptEngine javascriptEngine;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public APIResponse addForeWarning(HttpServletRequest request) throws IOException {
        String nodeCode = request.getParameter("node");
        Node node = nodeService.getNode(nodeCode);

        Forewarning forewarning = new Forewarning();
        forewarning.setId(UUID.randomUUID().toString());
        forewarning.setTitle(request.getParameter("title"));
        forewarning.setMetric(request.getParameter("metric"));
        forewarning.setSnippet(request.getParameter("snippet"));
        forewarning.setMsg(request.getParameter("msg"));

        try {
            String context = nodeService.getNodeMetricContext(node, forewarning.getMetric());
            javascriptEngine.executeScript(context, forewarning.getSnippet());
        } catch (ScriptException e) {
            return APIResponse.buildErrorResponse(ExceptionUtils.getRootCause(e).getMessage());
        }

        List<String> notifiers = new ArrayList<String>();
        forewarning.setNotifiers(notifiers);
        String[] notifierList = request.getParameterMap().get("notifiers[]");
        if (notifierList != null) {
            for (String notifierId : notifierList) {
                Notifier notifier = notifierService.getNotifier(notifierId);
                if (notifier != null) {
                    notifiers.add(notifier.getId());
                }
            }
        }

        IFireRule firerule = ((IFireRule) applicationContext.getBean(request.getParameter("firerule")));
        if (firerule == null) {
            APIResponse.buildErrorResponse("fire rule doesn't exist");
        }
        forewarning.setFireRule(request.getParameter("firerule"));

        node.getForewarnings().add(forewarning);

        configService.save();

        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/delete/", method = RequestMethod.POST)
    public APIResponse deleteForeWarning(HttpServletRequest request) throws IOException {
        String nodeCode = request.getParameter("node");
        Node node = nodeService.getNode(nodeCode);
        String metric = request.getParameter("metric");

        Forewarning deletedForewarning = null;
        for (Forewarning forewarning : node.getForewarnings()) {
            if (forewarning.getMetric().equals(metric)) {
                deletedForewarning = forewarning;
            }
        }
        if (deletedForewarning != null) {
            node.getForewarnings().remove(deletedForewarning);
        }

        configService.save();

        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/evaluate/", method = RequestMethod.POST)
    public APIResponse evaluate(HttpServletRequest request) throws IOException {
        String nodeCode = request.getParameter("node");
        Node node = nodeService.getNode(nodeCode);
        String metric = request.getParameter("metric");
        String snippet = request.getParameter("snippet");
        if (StringUtils.isEmpty(snippet)) return APIResponse.buildSuccessResponse("Please input snippet");

        try {
            String context = nodeService.getNodeMetricContext(node, metric);
            return APIResponse.buildSuccessResponse(javascriptEngine.executeScript(context, snippet).toString());
        } catch (ScriptException e) {
            return APIResponse.buildSuccessResponse(ExceptionUtils.getRootCause(e).getMessage());
        }
    }
}