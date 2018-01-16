package monitorx.config.plugin;

import org.pf4j.PluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginConfig {

    @Bean
    public PluginManager pluginManager() {
        return new MonitorXSpringPluginManager();
    }
}