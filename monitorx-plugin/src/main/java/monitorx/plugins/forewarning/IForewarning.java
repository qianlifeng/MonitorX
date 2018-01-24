package monitorx.plugins.forewarning;

import org.pf4j.ExtensionPoint;

/**
 * @author qianlifeng
 */
public interface IForewarning extends ExtensionPoint {
    /**
     * forewarning code
     */
    String getCode();

    /**
     * forewarning name
     */
    String getName();

    /**
     * forewarning description
     */
    String getDescription();

    /**
     * should warning or not
     *
     * @param context forewarning context
     */
    boolean shouldWarning(ForewarningContext context);

    IForewarningConfig getForewarningConfig();
}