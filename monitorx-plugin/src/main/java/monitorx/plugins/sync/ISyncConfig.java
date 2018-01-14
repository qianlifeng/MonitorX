package monitorx.plugins.sync;

import org.pf4j.ExtensionPoint;

/**
 * Sync config
 * any sync config bean should implement this interface
 * all files defined in SyncConfig will be serialized to json file, so make sure define simple files that can be serialized to json
 *
 * @author qianlifeng
 */
public interface ISyncConfig extends ExtensionPoint {

}