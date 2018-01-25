package monitorx.domain;

import monitorx.plugins.notifier.INotifierConfig;

/**
 * @author qianlifeng
 */
public class Notifier {
    String id;
    String title;
    String notifierCode;
    String fontawesomeIcon;
    INotifierConfig notifierConfig;

    public String getFontawesomeIcon() {
        return fontawesomeIcon;
    }

    public void setFontawesomeIcon(String fontawesomeIcon) {
        this.fontawesomeIcon = fontawesomeIcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotifierCode() {
        return notifierCode;
    }

    public void setNotifierCode(String notifierCode) {
        this.notifierCode = notifierCode;
    }

    public INotifierConfig getNotifierConfig() {
        return notifierConfig;
    }

    public void setNotifierConfig(INotifierConfig notifierConfig) {
        this.notifierConfig = notifierConfig;
    }
}