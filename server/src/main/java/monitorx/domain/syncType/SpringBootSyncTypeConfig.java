package monitorx.domain.syncType;

import org.springframework.stereotype.Component;

@Component("syncTypeConfig-springboot")
public class SpringBootSyncTypeConfig implements ISyncTypeConfig {
    /**
     * Seconds
     */
    int interval = 3;

    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}