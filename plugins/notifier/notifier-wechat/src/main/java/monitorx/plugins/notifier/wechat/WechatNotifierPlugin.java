package monitorx.plugins.notifier.wechat;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qianlifeng
 */
public class WechatNotifierPlugin extends Plugin {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public WechatNotifierPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        logger.info("WechatNotifierPlugin.start()");
    }

    @Override
    public void stop() {
        logger.info("WechatNotifierPlugin.stop()");
    }
}