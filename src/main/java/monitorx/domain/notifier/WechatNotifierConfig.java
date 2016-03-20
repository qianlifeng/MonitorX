package monitorx.domain.notifier;

import org.apache.commons.lang3.StringUtils;
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
        logger.info("sending wechat msg:" + title);
        String url = "http://sc.ftqq.com/" + secret + ".send";
        try {
            Form form = Form.form().add("text", title);
            if (StringUtils.isNotEmpty(msg)) {
                //add timestamp to avoid wechat prevent same msg in a short time
                form.add("desp", msg + "\n\n" + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " from MonitorX");
            }
            Request.Post(url).bodyForm(form.build(), Consts.UTF_8).execute();
        } catch (IOException e) {
            logger.error("Send wechat msg failed", e);
        }
    }
}