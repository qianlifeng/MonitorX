package monitorx.service;

import monitorx.domain.Metric;
import monitorx.domain.Node;
import monitorx.domain.forewarning.Forewarning;
import monitorx.domain.forewarning.ForewarningCheckPoint;
import monitorx.domain.forewarning.IFireRule;
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
    }

    public String getNodeMetricContext(Node node, String metricTitle) {
        if (StringUtils.isEmpty(metricTitle)) {
            //get node context
            return "var isUp = " + node.getStatus().getStatus().equals("up") + ";";
        }

        for (Metric metric : node.getStatus().getMetrics()) {
            if (metric.getTitle().equals(metricTitle)) {
                String context = "var value = " + metric.getValue() + ";";
                if (StringUtils.isNotEmpty(metric.getContext())) {
                    context += metric.getContext();
                }

                return context;
            }
        }

        return StringUtils.EMPTY;
    }

    public void checkForewarningAndNotify(Node node) {
        for (Forewarning forewarning : node.getForewarnings()) {
            boolean isFireNotify = ((IFireRule) applicationContext.getBean(forewarning.getFireRule())).isSatisfied(forewarning.getFireRuleContext());
            if (isFireNotify) {
                for (String notifier : forewarning.getNotifiers()) {
                    notifierService.getNotifier(notifier).send(forewarning.getTitle(), "node:" + node.getTitle() + ", metric=" + forewarning.getMetric());
                }
            }
        }
    }
}