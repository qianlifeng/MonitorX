package monitorx.domain.forewarning;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class Forewarning {

    String id;

    String title;

    List<String> notifiers;

    /**
     * Javascript snippet that created by user
     * this snippet will be return a boolean result which indicates if it should fire forewarning
     */
    String snippet;

    /**
     * If a forewarning is applied to metric, this field will indicate which metric it target to
     * If target is empty, which means this forewarning target to the node itself
     */
    String metric;

    /**
     * If a forewarning specifies firerule, MonitorX will only send a forewarning if satisfied firerule
     * Otherwise, MonitorX will send the forewarning immediately
     */
    String fireRule;

    IFireRuleConfig fireRuleConfig;

    String msg;

    @JSONField(serialize = false)
    FireRuleContext fireRuleContext = new FireRuleContext();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getNotifiers() {
        return notifiers;
    }

    public void setNotifiers(List<String> notifiers) {
        this.notifiers = notifiers;
    }

    public String getFireRule() {
        return fireRule;
    }

    public void setFireRule(String fireRule) {
        this.fireRule = fireRule;
    }

    public FireRuleContext getFireRuleContext() {
        return fireRuleContext;
    }

    public void setFireRuleContext(FireRuleContext fireRuleContext) {
        this.fireRuleContext = fireRuleContext;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String target) {
        this.metric = target;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public IFireRuleConfig getFireRuleConfig() {
        return fireRuleConfig;
    }

    public void setFireRuleConfig(IFireRuleConfig fireRuleConfig) {
        this.fireRuleConfig = fireRuleConfig;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
