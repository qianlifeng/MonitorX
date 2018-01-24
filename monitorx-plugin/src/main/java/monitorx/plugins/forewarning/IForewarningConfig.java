package monitorx.plugins.forewarning;

import org.pf4j.ExtensionPoint;

/**
 * Forewarning config
 * any forewarning config bean should implement this interface
 * all files defined in ForewarningConfig will be serialized to json file, so make sure define simple files that can be serialized to json
 *
 * @author qianlifeng
 */
public interface IForewarningConfig extends ExtensionPoint {

}