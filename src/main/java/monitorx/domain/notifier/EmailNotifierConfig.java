package monitorx.domain.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("notifierConfig-email")
public class EmailNotifierConfig implements INotifierConfig {
    Logger logger = LoggerFactory.getLogger(getClass());
    String toEmail;

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public void send(String title, String msg) {

    }
}