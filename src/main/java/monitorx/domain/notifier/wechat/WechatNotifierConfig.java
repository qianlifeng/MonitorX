package monitorx.domain.notifier.wechat;

import monitorx.domain.notifier.INotifierConfig;
import org.springframework.stereotype.Component;

@Component("notifierConfig-wechat")
public class WechatNotifierConfig implements INotifierConfig {

    /**
     * Get secret from http://sc.ftqq.com/2.version
     */
    String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}