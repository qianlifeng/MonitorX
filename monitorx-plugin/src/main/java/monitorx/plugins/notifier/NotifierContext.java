package monitorx.plugins.notifier;

/**
 * @author qianlifeng
 */
public class NotifierContext {
    INotifierConfig notifierConfig;
    String title;
    String content;

    public INotifierConfig getNotifierConfig() {
        return notifierConfig;
    }

    public NotifierContext(INotifierConfig notifierConfig, String title, String content) {
        this.notifierConfig = notifierConfig;
        this.title = title;
        this.content = content;
    }

    public NotifierContext() {
    }

    public void setNotifierConfig(INotifierConfig notifierConfig) {
        this.notifierConfig = notifierConfig;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}