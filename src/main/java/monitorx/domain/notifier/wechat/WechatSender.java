package monitorx.domain.notifier.wechat;

import monitorx.domain.notifier.ISender;
import org.springframework.stereotype.Component;

@Component("sender-wechat")
public class WechatSender implements ISender {

    public void send(String title, String msg) {

    }
}