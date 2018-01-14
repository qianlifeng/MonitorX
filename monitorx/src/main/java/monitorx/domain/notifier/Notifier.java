package monitorx.domain.notifier;

public class Notifier {
    String id;
    String title;
    /**
     * @see NotifierTypeEnum
     */
    String type;

    INotifierConfig config;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public INotifierConfig getConfig() {
        return config;
    }

    public void setConfig(INotifierConfig config) {
        this.config = config;
    }

    public void send(String title, String content) {
        config.send(title, content);
    }
}