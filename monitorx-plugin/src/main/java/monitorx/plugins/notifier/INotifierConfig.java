package monitorx.plugins.notifier;

import org.pf4j.ExtensionPoint;

/**
 * Notifier config
 * any notifier config bean should implement this interface
 * all files defined in notifierConfig will be serialized to json file, so make sure define simple files that can be serialized to json
 *
 * @author qianlifeng
 */
public interface INotifierConfig extends ExtensionPoint {
}