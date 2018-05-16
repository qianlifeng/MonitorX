package monitorx.plugins.notifier.wechat;

import monitorx.plugins.notifier.INotifier;
import monitorx.plugins.notifier.INotifierConfig;
import monitorx.plugins.notifier.NotifierContext;
import org.apache.http.Consts;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.pf4j.Extension;
import org.pf4j.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author qianlifeng
 */
@Extension
public class WechatNotifier implements INotifier {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String getCode() {
        return "notifier-wechat";
    }

    @Override
    public String getName() {
        return "Wechat";
    }

    @Override
    public String getDescription() {
        return "MonitorX use <a target='_blank' href='http://sc.ftqq.com'>Server酱</a> to send wechat message, you can get secret there";
    }

    @Override
    public String getFontAwesomeIcon() {
        return "fa-weixin";
    }

    @Override
    public void send(NotifierContext context) {
        WechatNotifierConfig config = (WechatNotifierConfig) context.getNotifierConfig();
        logger.info("sending wechat msg:" + context.getTitle());
        String url = "https://sc.ftqq.com/" + config.getSecret() + ".send";
        try {
            Form form = Form.form().add("text", context.getTitle());
            if (StringUtils.isNotNullOrEmpty(context.getContent())) {
                //add timestamp to avoid wechat prevent same msg in a short time
                form.add("desp", context.getContent() + "\n\n");
            }
            Request.Post(url).bodyForm(form.build(), Consts.UTF_8).execute();
        } catch (IOException e) {
            logger.error("Send wechat msg failed", e);
        }
    }

    @Override
    public INotifierConfig getNotifierConfig() {
        return new WechatNotifierConfig();
    }
}