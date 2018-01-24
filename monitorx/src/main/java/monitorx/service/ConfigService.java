package monitorx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.Node;
import monitorx.domain.Notifier;
import monitorx.domain.config.Config;
import monitorx.plugins.forewarning.IForewarning;
import monitorx.plugins.forewarning.IForewarningConfig;
import monitorx.plugins.notifier.INotifier;
import monitorx.plugins.notifier.INotifierConfig;
import monitorx.plugins.sync.ISync;
import monitorx.plugins.sync.ISyncConfig;
import org.apache.commons.io.FileUtils;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ConfigService {

    static String CONFIG_NAME = "config.json";
    static Config config;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PluginManager pluginManager;

    public Config getConfig() {
        if (config != null) {
            return config;
        }

        File file = new File(CONFIG_NAME);
        if (file.exists()) {
            try {
                String configJSON = FileUtils.readFileToString(file, "UTF-8");
                config = JSON.parseObject(configJSON, Config.class);
                rebuildNotifier(config, configJSON);
                rebuildSync(config, configJSON);
                rebuildForewarning(config, configJSON);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //load default config
            config = JSON.parseObject(JSON.toJSONString(new Config()), Config.class);
        }

        return config;
    }

    private void rebuildNotifier(Config config, String configJSON) {
        JSONArray notifiers = JSON.parseObject(configJSON).getJSONArray("notifiers");

        for (Notifier notifier : config.getNotifiers()) {
            for (Object obj : notifiers) {
                JSONObject notifierJSON = ((JSONObject) obj);
                if (notifierJSON.get("id").equals(notifier.getId())) {
                    pluginManager.getExtensions(INotifier.class).stream().filter(o -> o.getCode().equals(notifier.getNotifierCode())).findFirst().ifPresent(n -> {
                        INotifierConfig configClass = n.getNotifierConfig();
                        INotifierConfig notifierConfig = JSON.parseObject(JSON.toJSONString(notifierJSON.get("config")), configClass.getClass());
                        notifier.setNotifierConfig(notifierConfig);
                    });
                }
            }
        }
    }

    private void rebuildSync(Config config, String configJSON) {
        JSONArray nodes = JSON.parseObject(configJSON).getJSONArray("nodes");

        for (Node node : config.getNodes()) {
            for (Object obj : nodes) {
                JSONObject nodeJson = ((JSONObject) obj);
                if (nodeJson.get("code").equals(node.getCode())) {
                    pluginManager.getExtensions(ISync.class).stream().filter(o -> o.getCode().equals(node.getSync())).findFirst().ifPresent(sync -> {
                        ISyncConfig syncConfig = sync.getSyncConfig();
                        ISyncConfig notifierConfig = JSON.parseObject(JSON.toJSONString(nodeJson.get("syncConfig")), syncConfig.getClass());
                        node.setSyncConfig(notifierConfig);
                    });
                }
            }
        }
    }

    private void rebuildForewarning(Config config, String configJSON) {
        JSONArray nodes = JSON.parseObject(configJSON).getJSONArray("nodes");

        for (Node node : config.getNodes()) {
            for (Object obj : nodes) {
                JSONObject nodeJson = ((JSONObject) obj);
                if (nodeJson.get("code").equals(node.getCode())) {
                    nodeJson.getJSONArray("forewarnings").forEach(f -> {
                        JSONObject forewarningJSONConfig = ((JSONObject) f).getJSONObject("forewarningConfig");
                        node.getForewarnings().stream().filter(nf -> nf.getId().equals(((JSONObject) f).getString("id"))).forEach(forewarning -> {
                            pluginManager.getExtensions(IForewarning.class).stream().filter(o -> o.getCode().equals(forewarning.getForewarningCode())).findFirst().ifPresent(f1 -> {
                                IForewarningConfig forewarningConfig = JSON.parseObject(JSON.toJSONString(forewarningJSONConfig), f1.getForewarningConfig().getClass());
                                forewarning.setForewarningConfig(forewarningConfig);
                            });
                        });
                    });
                }
            }
        }
    }

    public void save() {
        if (config != null) {
            String configString = JSON.toJSONString(config, true);
            try {
                FileUtils.writeStringToFile(new File(CONFIG_NAME), configString, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
