package monitorx.domain.notifier;

import org.apache.http.Consts;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

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
        logger.info("sending msg:" + title);
        String url = "http://sc.ftqq.com/" + secret + ".send";
        try {
            Request.Post(url)
                    .bodyForm(Form.form()
                            .add("text", title)
                            .add("desp", msg+"\n"+ DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss")).build(), Consts.UTF_8).execute();
        } catch (IOException e) {
            logger.error("Send wechat msg failed", e);
        }
    }
}