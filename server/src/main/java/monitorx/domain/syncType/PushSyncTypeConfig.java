package monitorx.domain.syncType;

import org.springframework.stereotype.Component;

@Component("syncTypeConfig-push")
public class PushSyncTypeConfig implements ISyncTypeConfig {
    /**
     * Seconds that MonitorX tolerate node didn't push status
     */
    private Integer interval;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }
}
