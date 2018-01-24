package monitorx.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.Forewarning;
import monitorx.domain.Node;
import monitorx.domain.Notifier;
import monitorx.plugins.annotation.UIField;
import monitorx.plugins.forewarning.IForewarning;
import monitorx.plugins.forewarning.IForewarningConfig;
import monitorx.service.ConfigService;
import monitorx.service.JavascriptEngine;
import monitorx.service.NodeService;
import monitorx.service.NotifierService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

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

    @Autowired
    PluginManager pluginManager;

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
        forewarning.setRecoveredMsg(request.getParameter("recoveredMsg"));

        pluginManager.getExtensions(IForewarning.class).stream().filter(o -> o.getCode().equalsIgnoreCase(request.getParameter("forewarningCode"))).findFirst().ifPresent(f -> {
            forewarning.setForewarningCode(f.getCode());
            IForewarningConfig forewarningConfig = JSON.parseObject(request.getParameter("forewarningConfig"), f.getForewarningConfig().getClass());
            forewarning.setForewarningConfig(forewarningConfig);
        });

        try {
            String context = nodeService.getNodeMetricContext(node, forewarning.getMetric());
            Object returnValue = javascriptEngine.executeScript(context, forewarning.getSnippet());
            if (returnValue == null) return APIResponse.buildErrorResponse("Snippet doesn't return any value");
        } catch (ScriptException e) {
            return APIResponse.buildErrorResponse(ExceptionUtils.getRootCauseMessage(e));
        } catch (Exception e) {
            return APIResponse.buildErrorResponse(ExceptionUtils.getRootCauseMessage(e));
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


        node.getForewarnings().add(forewarning);

        configService.save();

        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/edit/", method = RequestMethod.POST)
    public APIResponse editForeWarning(HttpServletRequest request) throws IOException {
        String forewarningId = request.getParameter("forewarningId");
        String nodeCode = request.getParameter("node");
        Node node = nodeService.getNode(nodeCode);

        Forewarning existingForewarning = nodeService.findForewarning(nodeCode, forewarningId);
        existingForewarning.setTitle(request.getParameter("title"));
        existingForewarning.setSnippet(request.getParameter("snippet"));
        existingForewarning.setMsg(request.getParameter("msg"));
        existingForewarning.setRecoveredMsg(request.getParameter("recoveredMsg"));

        pluginManager.getExtensions(IForewarning.class).stream().filter(o -> o.getCode().equalsIgnoreCase(request.getParameter("forewarningCode"))).findFirst().ifPresent(f -> {
            existingForewarning.setForewarningCode(f.getCode());
            IForewarningConfig forewarningConfig = JSON.parseObject(request.getParameter("forewarningConfig"), f.getForewarningConfig().getClass());
            existingForewarning.setForewarningConfig(forewarningConfig);
        });

        try {
            String context = nodeService.getNodeMetricContext(node, existingForewarning.getMetric());
            Object returnValue = javascriptEngine.executeScript(context, existingForewarning.getSnippet());
            if (returnValue == null) return APIResponse.buildErrorResponse("Snippet doesn't return any value");
        } catch (ScriptException e) {
            return APIResponse.buildErrorResponse(ExceptionUtils.getRootCauseMessage(e));
        } catch (Exception e) {
            return APIResponse.buildErrorResponse(ExceptionUtils.getRootCauseMessage(e));
        }

        List<String> notifiers = new ArrayList<String>();
        existingForewarning.setNotifiers(notifiers);
        String[] notifierList = request.getParameterMap().get("notifiers[]");
        if (notifierList != null) {
            for (String notifierId : notifierList) {
                Notifier notifier = notifierService.getNotifier(notifierId);
                if (notifier != null) {
                    notifiers.add(notifier.getId());
                }
            }
        }


        configService.save();
        return APIResponse.buildSuccessResponse();
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public APIResponse getForewarning(HttpServletRequest request) throws IOException {
        String nodeCode = request.getParameter("node");
        String forewarningId = request.getParameter("forewarning");
        return APIResponse.buildSuccessResponse(nodeService.findForewarning(nodeCode, forewarningId));
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
        if (StringUtils.isEmpty(snippet)) {
            return APIResponse.buildSuccessResponse("Please input snippet");
        }

        try {
            String context = nodeService.getNodeMetricContext(node, metric);
            Object returnValue = javascriptEngine.executeScript(context, snippet);
            if (returnValue == null) {
                return APIResponse.buildSuccessResponse("Snippet doesn't return any value");
            }
            return APIResponse.buildSuccessResponse(escapeHtml4(returnValue.toString()));
        } catch (Exception e) {
            return APIResponse.buildSuccessResponse(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    @RequestMapping(value = "/context/", method = RequestMethod.GET)
    public APIResponse getForewarningContext(HttpServletRequest request) throws IOException {
        String nodeCode = request.getParameter("node");
        String metric = request.getParameter("metric");
        Node node = nodeService.getNode(nodeCode);
        String context = nodeService.getNodeMetricContext(node, metric);
        return APIResponse.buildSuccessResponse(context);
    }

    @RequestMapping(value = "/previewMsg/", method = RequestMethod.POST)
    public APIResponse previewMsg(HttpServletRequest request) throws IOException {
        String nodeCode = request.getParameter("node");
        Node node = nodeService.getNode(nodeCode);
        String metric = request.getParameter("metric");
        String msg = request.getParameter("msg");
        if (StringUtils.isEmpty(msg)) {
            return APIResponse.buildSuccessResponse("Please input msg");
        }

        try {
            String realMsg = nodeService.getTranslatedMsg(node, metric, msg);
            return APIResponse.buildSuccessResponse(realMsg);
        } catch (Exception e) {
            return APIResponse.buildSuccessResponse(ExceptionUtils.getRootCauseMessage(e));
        }
    }

    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    public APIResponse getForewarningRuleList() {
        List<JSONObject> forewarnings = pluginManager.getExtensions(IForewarning.class).stream().map(o -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", o.getCode());
            jsonObject.put("name", o.getName());
            jsonObject.put("description", o.getDescription());
            JSONArray configs = new JSONArray();
            for (Field configField : o.getForewarningConfig().getClass().getDeclaredFields()) {
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
        return APIResponse.buildSuccessResponse(forewarnings);
    }
}