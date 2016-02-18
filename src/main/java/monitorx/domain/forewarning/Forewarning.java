package monitorx.domain.forewarning;

import monitorx.domain.notifier.Notifier;

public class Forewarning {
    Notifier notifier;

    /**
     * Javascript snippet that created by user
     * this snippet will be return a boolean result which indicates if it should fire forewarning
     */
    String snippet;

    /**
     * If a forewarning is applied to metric, this field will indicate which metric it target to
     * If target is empty, which means this forewarning target to the node itself
     */
    String target;

    /**
     * If a forewarning specifies firerule, MonitorX will only send a forewarning if satisfied firerule
     * Otherwise, MonitorX will send the forewarning immediately
     */
    IFireRule fireRule;

    public IFireRule getFireRule() {
        return fireRule;
    }

    public void setFireRule(IFireRule fireRule) {
        this.fireRule = fireRule;
    }

    public Notifier getNotifier() {
        return notifier;
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
