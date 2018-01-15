package monitorx.plugins.sync;

import monitorx.plugins.Status;
import org.pf4j.ExtensionPoint;

/**
 * @author qianlifeng
 */
public interface ISync extends ExtensionPoint {
    /**
     * sync code
     */
    String getCode();

    /**
     * sync version
     * if plugin author make break changes to the sync plugin, version must get upgraded to disable to old one
     */
    int getVersion();

    /**
     * sync status every 5 seconds
     *
     * @param config sync config
     * @return sync status
     */
    Status sync(ISyncConfig config);

    ISyncConfig getSyncConfig();
}