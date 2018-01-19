package monitorx.plugins.sync.springboot;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qianlifeng
 */
public class SpringBootSyncPlugin extends Plugin {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public SpringBootSyncPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        logger.info("SpringBootSyncPlugin.start()");
    }

    @Override
    public void stop() {
        logger.info("SpringBootSyncPlugin.stop()");
    }
}