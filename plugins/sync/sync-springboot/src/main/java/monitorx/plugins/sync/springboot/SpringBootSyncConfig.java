package monitorx.plugins.sync.springboot;


import monitorx.plugins.annotation.UIField;
import monitorx.plugins.sync.ISyncConfig;
import org.pf4j.Extension;

/**
 * @author qianlifeng
 */
@Extension
public class SpringBootSyncConfig implements ISyncConfig {

    /**
     * Spring Actuator Url
     */
    @UIField(code = "url", name = "Spring Actuator Url", description = "")
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}