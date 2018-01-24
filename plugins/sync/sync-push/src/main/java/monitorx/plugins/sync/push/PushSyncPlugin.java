package monitorx.plugins.sync.push;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author qianlifeng
 */
public class PushSyncPlugin extends Plugin {
    private Logger logger = LoggerFactory.getLogger(getClass());

    public PushSyncPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        logger.info("PushSyncPlugin.start()");
    }

    @Override
    public void stop() {
        logger.info("PushSyncPlugin.stop()");
    }
}