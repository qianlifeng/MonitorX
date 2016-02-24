package monitorx.domain.notifier;

import org.apache.http.Consts;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("notifierConfig-callback")
public class CallbackNotifierConfig implements INotifierConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    String callbackUrl;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void send(String title, String msg) {
        logger.info("sending callback msg:" + title);
        try {
            Request.Post(callbackUrl).bodyForm(Form.form().add("msg", msg).build(), Consts.UTF_8).execute();
        } catch (IOException e) {
            logger.error("Send callback msg failed", e);
        }
    }
}