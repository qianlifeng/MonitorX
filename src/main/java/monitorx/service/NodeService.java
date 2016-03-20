package monitorx.service;

import monitorx.domain.Metric;
import monitorx.domain.Node;
import monitorx.domain.forewarning.Forewarning;
import monitorx.domain.forewarning.ForewarningCheckPoint;
import monitorx.domain.forewarning.IFireRule;
import monitorx.domain.notifier.Notifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NodeService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ConfigService configService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    JavascriptEngine javascriptEngine;

    @Autowired
    NotifierService notifierService;

    public List<Node> getNodes() {
        return configService.getConfig().getNodes();
    }

    public void addNode(Node node) {
        configService.getConfig().getNodes().add(node);
        configService.save();
    }

    public Node getNode(String nodeCode) {
        for (Node node : getNodes()) {
            if (node.getCode().equals(nodeCode)) {
                return node;
            }
        }

        return null;
    }

    public void removeNode(String nodeCode) {
        Node node = getNode(nodeCode);
        if (node != null) {
            getNodes().remove(node);
            configService.save();
        }
    }

    public Forewarning findForewarning(String nodeCode, String forewarningId) {
        Node node = getNode(nodeCode);
        for (Forewarning forewarning : node.getForewarnings()) {
            if (forewarning.getId().equals(forewarningId)) return forewarning;
        }

        return null;
    }

    public Forewarning findForewarningByTitle(String nodeCode, String forewarningTitle) {
        Node node = getNode(nodeCode);
        for (Forewarning forewarning : node.getForewarnings()) {
            if (forewarning.getTitle().equals(forewarningTitle)) return forewarning;
        }

        return null;
    }

    public void addCheckPoints(Node node) {
        for (Forewarning forewarning : node.getForewarnings()) {
            ForewarningCheckPoint checkPoint = new ForewarningCheckPoint();
            checkPoint.setDatetime(new Date());
            try {
                String context = getNodeMetricContext(node, forewarning.getMetric());
                Boolean result = (Boolean) javascriptEngine.executeScript(context, forewarning.getSnippet());
                checkPoint.setSnippetResult(result);
            } catch (ScriptException e) {
                logger.warn("Execute javascript failed while adding checkpoint," + ExceptionUtils.getRootCauseMessage(e));
                return;
            }

            forewarning.getFireRuleContext().addCheckPoint(checkPoint);
        }

        checkForewarningAndNotify(node);
        checkIfNodeRecoveredAndNotify(node);
    }

    private void checkIfNodeRecoveredAndNotify(Node node) {
        for (Forewarning forewarning : node.getForewarnings()) {
            if (isRecovered(forewarning) && StringUtils.isNotEmpty(forewarning.getRecoveredMsg())) {
                logger.info("sending recovered msg for node:" + node.getCode());
                String msg = forewarning.getRecoveredMsg();
                try {
                    msg = getTranslatedMsg(node, forewarning.getMetric(), forewarning.getRecoveredMsg());
                } catch (Exception e) {
                    //no-op
                }
                for (String notifierId : forewarning.getNotifiers()) {
                    Notifier notifier = notifierService.getNotifier(notifierId);
                    if (notifier != null) {
                        notifier.send(msg, StringUtils.EMPTY);
                    }
                }
            }
        }
    }

    /**
     * check if a node is recovered from offline status
     */
    private boolean isRecovered(Forewarning forewarning) {
        List<ForewarningCheckPoint> checkPoints = forewarning.getFireRuleContext().getCheckPoints();
        if (checkPoints != null && checkPoints.size() > 1) {
            ForewarningCheckPoint lastCheckPoint = checkPoints.get(checkPoints.size() - 1);
            ForewarningCheckPoint LastTwoCheckPoint = checkPoints.get(checkPoints.size() - 2);
            if (!lastCheckPoint.getSnippetResult() && LastTwoCheckPoint.getSnippetResult()) {
                return true;
            }
        }
        return false;
    }

    public String getNodeMetricContext(Node node, String metricTitle) {
        if (StringUtils.isEmpty(metricTitle)) {
            //get node context
            String isUp = "false";
            if (node.getStatus() != null) {
                isUp = String.valueOf(node.getStatus().getStatus().equals("up"));
            }
            return "var isUp = " + isUp + ";";
        }

        if (node.getStatus() != null) {
            for (Metric metric : node.getStatus().getMetrics()) {
                if (metric.getTitle().equals(metricTitle)) {
                    String context = "var value = " + metric.getValue() + ";";
                    if (StringUtils.isNotEmpty(metric.getContext())) {
                        context += "var contextJSON=" + metric.getContext() + ";";
                    }

                    return context;
                }
            }
        }

        return StringUtils.EMPTY;
    }

    public String getTranslatedMsg(Node node, String metricTitle, String originMsg) throws ScriptException {
        String context = getNodeMetricContext(node, metricTitle);

        Matcher m = Pattern.compile("\\{\\{(.*?)\\}\\}").matcher(originMsg);
        String msg = originMsg;
        while (m.find()) {
            String expression = m.group(1);
            String value = javascriptEngine.executeScript(context, "return " + expression).toString();
            msg = msg.replace("{{" + expression + "}}", value);
        }

        return msg;
    }

    public void checkForewarningAndNotify(Node node) {
        for (Forewarning forewarning : node.getForewarnings()) {
            boolean shouldNotify = ((IFireRule) applicationContext.getBean(forewarning.getFireRule())).shouldFireNotify(forewarning.getFireRuleContext());
            if (shouldNotify) {
                String msg = forewarning.getMsg();
                try {
                    msg = getTranslatedMsg(node, forewarning.getMetric(), forewarning.getMsg());
                } catch (Exception e) {
                    //no-op
                }
                for (String notifierId : forewarning.getNotifiers()) {
                    Notifier notifier = notifierService.getNotifier(notifierId);
                    if (notifier != null) {
                        List<ForewarningCheckPoint> checkPoints = forewarning.getFireRuleContext().getCheckPoints();
                        ForewarningCheckPoint lastCheckPoint = checkPoints.get(checkPoints.size() - 1);
                        lastCheckPoint.setHasSendNotify(true);
                        notifier.send(msg, StringUtils.EMPTY);
                    }
                }
            }
        }
    }
}