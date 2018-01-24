package monitorx.plugins.notifier;

import org.pf4j.ExtensionPoint;

/**
 * @author qianlifeng
 */
public interface INotifier extends ExtensionPoint {
    /**
     * notifier code
     */
    String getCode();

    /**
     * notifier name
     */
    String getName();

    /**
     * notifier description
     */
    String getDescription();

    /**
     * fontawesomne icon that will display in notifier block
     * e.g. fa-weixin
     */
    String getFontAwesomeIcon();

    /**
     * send notify msg
     */
    void send(NotifierContext context);

    INotifierConfig getNotifierConfig();
}