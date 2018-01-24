package monitorx.plugins.forewarning.immediate;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qianlifeng
 */
public class ImmediateForewarningPlugin extends Plugin {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ImmediateForewarningPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        logger.info("ImmediateForewarningPlugin.start()");
    }

    @Override
    public void stop() {
        logger.info("ImmediateForewarningPlugin.stop()");
    }
}