package monitorx.plugins.sync.push;


import monitorx.plugins.annotation.UIField;
import monitorx.plugins.sync.ISyncConfig;
import org.pf4j.Extension;

/**
 * @author qianlifeng
 */
@Extension
public class PushSyncConfig implements ISyncConfig {

    /**
     * interval (seconds) that server push status to MonitorX
     */
    @UIField(code = "pushInterval", name = "Push Interval", description = "If this node didn't push its status to MonitorX every x seconds, MonitorX will consider it as down")
    int pushInterval = 10;

    public int getPushInterval() {
        return pushInterval;
    }

    public void setPushInterval(int pushInterval) {
        this.pushInterval = pushInterval;
    }
}