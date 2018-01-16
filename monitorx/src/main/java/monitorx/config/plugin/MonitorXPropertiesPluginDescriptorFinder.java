package monitorx.config.plugin;

import org.pf4j.PluginException;
import org.pf4j.PropertiesPluginDescriptorFinder;
import org.pf4j.util.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MonitorXPropertiesPluginDescriptorFinder extends PropertiesPluginDescriptorFinder {

    @Override
    protected Path getPropertiesPath(Path pluginPath, String propertiesFileName) throws PluginException {
        if (Files.isDirectory(pluginPath)) {
            return pluginPath.resolve(Paths.get("classes", propertiesFileName));
        } else {
            // it's a jar file
            try {
                return FileUtils.getPath(pluginPath, propertiesFileName);
            } catch (IOException e) {
                throw new PluginException(e);
            }
        }
    }
}
