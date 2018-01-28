package monitorx.plugins.forewarning.delay;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qianlifeng
 */
public class DelayForewarningPlugin extends Plugin {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public DelayForewarningPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        logger.info("DelayForewarningPlugin.start()");
    }

    @Override
    public void stop() {
        logger.info("DelayForewarningPlugin.stop()");
    }
}