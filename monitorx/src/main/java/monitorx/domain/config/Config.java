package monitorx.domain.config;

import monitorx.domain.Node;
import monitorx.domain.Notifier;

import java.util.ArrayList;
import java.util.List;

public class Config {
    AppConfig app = new AppConfig();
    List<Node> nodes = new ArrayList<Node>();
    List<Notifier> notifiers = new ArrayList<Notifier>();

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public AppConfig getApp() {
        return app;
    }

    public void setApp(AppConfig app) {
        this.app = app;
    }

    public List<Notifier> getNotifiers() {
        return notifiers;
    }

    public void setNotifiers(List<Notifier> notifiers) {
        this.notifiers = notifiers;
    }
}
