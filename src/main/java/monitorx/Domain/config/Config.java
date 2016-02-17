package monitorx.domain.config;

import monitorx.domain.Node;

import java.util.ArrayList;
import java.util.List;

public class Config {
    AppConfig app = new AppConfig();
    List<Node> nodes = new ArrayList<Node>();

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

}
