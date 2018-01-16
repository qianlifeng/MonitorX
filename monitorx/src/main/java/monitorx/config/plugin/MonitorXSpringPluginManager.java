package monitorx.config.plugin;

import org.pf4j.*;
import org.pf4j.spring.SpringPluginManager;

public class MonitorXSpringPluginManager extends SpringPluginManager {

    @Override
    protected PluginDescriptorFinder createPluginDescriptorFinder() {
        return new MonitorXPropertiesPluginDescriptorFinder();
    }
}
