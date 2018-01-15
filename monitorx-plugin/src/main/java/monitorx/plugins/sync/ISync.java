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
     * sync name
     */
    String getName();

    /**
     * sync description
     */
    String getDescription();

    /**
     * sync status every n seconds
     */
    Status sync(SyncContext context);

    ISyncConfig getSyncConfig();
}