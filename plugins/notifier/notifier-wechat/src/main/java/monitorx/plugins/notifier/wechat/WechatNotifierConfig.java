package monitorx.plugins.notifier.wechat;


import monitorx.plugins.annotation.UIField;
import monitorx.plugins.notifier.INotifierConfig;
import org.pf4j.Extension;

/**
 * @author qianlifeng
 */
@Extension
public class WechatNotifierConfig implements INotifierConfig {

    /**
     * interval (seconds) that server push status to MonitorX
     */
    @UIField(code = "secret", name = "Secret", description = "Server酱 Secret")
    String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}