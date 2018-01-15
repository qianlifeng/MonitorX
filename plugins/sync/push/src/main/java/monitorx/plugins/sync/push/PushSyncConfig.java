package monitorx.plugins.sync.push;


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
    int pushInterval = 10;

    public int getPushInterval() {
        return pushInterval;
    }

    public void setPushInterval(int pushInterval) {
        this.pushInterval = pushInterval;
    }
}