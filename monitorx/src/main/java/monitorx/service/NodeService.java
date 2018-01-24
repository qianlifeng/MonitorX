package monitorx.service;

import monitorx.plugins.Metric;
import monitorx.domain.Node;
import monitorx.plugins.Status;
import monitorx.domain.Forewarning;
import monitorx.plugins.forewarning.ForewarningCheckPoint;
import monitorx.domain.Notifier;
import monitorx.plugins.forewarning.ForewarningContext;
import monitorx.plugins.forewarning.IForewarning;
import monitorx.plugins.notifier.INotifier;
import monitorx.plugins.notifier.NotifierContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.pf4j.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.util.*;
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

    @Autowired
    PluginManager pluginManager;

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


    public List<String> getLastMetricValueHistory(Metric metric, List<Status> statusHistory, int lastCount) {
        List<String> values = new ArrayList<String>();
        int stop = statusHistory.size() - lastCount > 0 ? statusHistory.size() - lastCount : 0;
        for (int i = statusHistory.size() - 1; i >= stop; i--) {
            Status nodeStatus = statusHistory.get(i);
            for (Metric m : nodeStatus.getMetrics()) {
                if (m.getTitle().equals(metric.getTitle())) {
                    values.add(m.getValue());
                }
            }
        }

        Collections.reverse(values);

        return values;
    }

    /**
     * 从最后开始每隔x分钟找一个点,总共 @param lastCount 个
     */
    public List<String> getLastMetricValueHistoryByTimeInterval(Metric metric, List<Status> statusHistory, int lastCount, int seconds) {
        boolean shouldBreak = false;
        List<String> values = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(statusHistory.get(statusHistory.size() - 1).getLastUpdateDate());
        cal.add(Calendar.MINUTE, 1);
        Date stopTime = removeSeconds(cal.getTime());

        for (int i = statusHistory.size() - 1; i >= 0; i--) {
            Status nodeStatus = statusHistory.get(i);
            if (removeSeconds(nodeStatus.getLastUpdateDate()).compareTo(stopTime) < 0) {
                for (Metric m : nodeStatus.getMetrics()) {
                    if (m.getTitle().equals(metric.getTitle())) {
                        values.add(m.getValue());

                        cal.setTime(stopTime);
                        cal.add(Calendar.SECOND, -seconds);
                        stopTime = cal.getTime();

                        if (values.size() >= lastCount) {
                            shouldBreak = true;
                            break;
                        }
                    }
                }

                if (shouldBreak) {
                    break;
                }
            }
        }

        Collections.reverse(values);

        return values;
    }

    private Date removeSeconds(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public Forewarning findForewarning(String nodeCode, String forewarningId) {
        Node node = getNode(nodeCode);
        for (Forewarning forewarning : node.getForewarnings()) {
            if (forewarning.getId().equals(forewarningId)) {
                return forewarning;
            }
        }

        return null;
    }

    public Forewarning findForewarningByTitle(String nodeCode, String forewarningTitle) {
        Node node = getNode(nodeCode);
        for (Forewarning forewarning : node.getForewarnings()) {
            if (forewarning.getTitle().equals(forewarningTitle)) {
                return forewarning;
            }
        }

        return null;
    }

    public void addCheckPoints(Node node) {
        for (Forewarning forewarning : node.getForewarnings()) {
            String context = getNodeMetricContext(node, forewarning.getMetric());

            try {
                ForewarningCheckPoint checkPoint = new ForewarningCheckPoint();
                checkPoint.setDatetime(new Date());
                if (StringUtils.isEmpty(context)) {
                    continue;
                }
                Boolean result = (Boolean) javascriptEngine.executeScript(context, forewarning.getSnippet());
                checkPoint.setSnippetResult(result);
                forewarning.getCheckPoints().add(checkPoint);
            } catch (ScriptException e) {
                logger.warn("Execute javascript failed while adding checkpoint,node=" + node.getCode() + ", metric=" + forewarning.getMetric() + ",context=" + context + ExceptionUtils.getRootCauseMessage(e));
            }
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
                        notifyMsg(notifier, msg);
                    }
                }
            }
        }
    }

    public void notifyMsg(Notifier notifier, String msg) {
        pluginManager.getExtensions(INotifier.class).stream().forEach(o -> {
            NotifierContext notifierContext = new NotifierContext(notifier.getNotifierConfig(), msg, StringUtils.EMPTY);
            o.send(notifierContext);
        });
    }

    /**
     * check if a node is recovered from offline status
     */
    private boolean isRecovered(Forewarning forewarning) {
        List<ForewarningCheckPoint> checkPoints = forewarning.getCheckPoints();
        if (checkPoints != null && checkPoints.size() > 1) {
            ForewarningCheckPoint lastCheckPoint = checkPoints.get(checkPoints.size() - 1);
            ForewarningCheckPoint LastTwoCheckPoint = checkPoints.get(checkPoints.size() - 2);
            if (!lastCheckPoint.getSnippetResult() && LastTwoCheckPoint.getSnippetResult()) {
                //above codes just satisfied the basic recovered rules (node is up now)
                //and we still need to find whether we have sent the notify msg between last up (snippetResult = false) checkpoint and current checkpoint
                //it only make senses that we send recover msg after we sent a status down msg
                if (hasSentNotifyMsgBeforeRecovery(checkPoints)) {
                    return true;
                } else {
                    logger.info("node is up, but we didn't sent notify msg after last up check, so we shouldn't fire recovery msg");
                }
            }
        }
        return false;
    }

    private boolean hasSentNotifyMsgBeforeRecovery(List<ForewarningCheckPoint> checkPoints) {
        int lastCheckpointIndex = checkPoints.size() - 1;
        int lastTwoCheckpointIndex = checkPoints.size() - 2;
        int lastUpCheckpointIndex = lastCheckpointIndex;

        for (int i = lastTwoCheckpointIndex; i >= 0; i--) {
            if (!checkPoints.get(i).getSnippetResult()) {
                lastUpCheckpointIndex = i;
                break;
            }
        }

        for (int i = lastUpCheckpointIndex; i <= lastCheckpointIndex; i++) {
            if (checkPoints.get(i).getHasSendNotify()) {
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
                    String metricValue = metric.getValue();
                    if (metric.getType().equals("text")) {
                        metricValue = "\"" + metricValue.replaceAll("\"", "\\\\\"") + "\"";
                    }

                    String context = "var value = " + metricValue + ";";
                    if (StringUtils.isNotEmpty(metric.getContext())) {
                        context = "var value=" + metric.getContext() + ";";
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
            if (forewarning.getCheckPoints().size() == 0) {
                continue;
            }
            ForewarningCheckPoint lastCheckPoint = forewarning.getCheckPoints().get(forewarning.getCheckPoints().size() - 1);

            pluginManager.getExtensions(IForewarning.class).stream().filter(o -> o.getCode().equalsIgnoreCase(forewarning.getForewarningCode())).findFirst().ifPresent(warning -> {
                ForewarningContext context = new ForewarningContext();
                context.setCheckPoints(forewarning.getCheckPoints());
                context.setForewarningConfig(forewarning.getForewarningConfig());
                if (warning.shouldWarning(context)) {
                    String msg = forewarning.getMsg();
                    try {
                        msg = getTranslatedMsg(node, forewarning.getMetric(), forewarning.getMsg());
                    } catch (Exception e) {
                        //no-op
                    }

                    for (String notifierId : forewarning.getNotifiers()) {
                        Notifier notifier = notifierService.getNotifier(notifierId);
                        if (notifier != null) {
                            notifyMsg(notifier, msg);
                            lastCheckPoint.setHasSendNotify(true);
                        }
                    }
                }
            });
        }
    }
}