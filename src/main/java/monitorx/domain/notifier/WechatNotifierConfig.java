package monitorx.domain.notifier;

import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("notifierConfig-wechat")
public class WechatNotifierConfig implements INotifierConfig {
    Logger logger = LoggerFactory.getLogger(getClass());

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

    public void send(String title, String msg) {
        String url = "http://sc.ftqq.com/" + secret + ".send";
        try {
            Request.Post(url)
                    .bodyForm(Form.form()
                            .add("text", title)
                            .add("desp", msg).build()).execute();
        } catch (IOException e) {
            logger.error("Send wechat msg failed", e);
        }
    }
}