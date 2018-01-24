package monitorx.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import monitorx.plugins.forewarning.ForewarningCheckPoint;
import monitorx.plugins.forewarning.IForewarningConfig;

import java.util.ArrayList;
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

    String forewarningCode;

    IForewarningConfig forewarningConfig;

    String msg;

    /**
     * msg if node/metric recovered from offline status
     */
    String recoveredMsg;

    @JsonIgnore
    @JSONField(serialize = false)
    List<ForewarningCheckPoint> checkPoints = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getForewarningCode() {
        return forewarningCode;
    }

    public void setForewarningCode(String forewarningCode) {
        this.forewarningCode = forewarningCode;
    }

    public IForewarningConfig getForewarningConfig() {
        return forewarningConfig;
    }

    public void setForewarningConfig(IForewarningConfig forewarningConfig) {
        this.forewarningConfig = forewarningConfig;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRecoveredMsg() {
        return recoveredMsg;
    }

    public void setRecoveredMsg(String recoveredMsg) {
        this.recoveredMsg = recoveredMsg;
    }

    public List<ForewarningCheckPoint> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(List<ForewarningCheckPoint> checkPoints) {
        this.checkPoints = checkPoints;
    }
}
