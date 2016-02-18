package monitorx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import monitorx.domain.config.Config;
import monitorx.domain.notifier.INotifierConfig;
import monitorx.domain.notifier.Notifier;
import org.apache.commons.io.FileUtils;
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

    public Config getConfig() {
        if (config != null) return config;

        File file = new File(CONFIG_NAME);
        if (file.exists()) {
            try {
                String configJSON = FileUtils.readFileToString(file);
                config = JSON.parseObject(configJSON, Config.class);
                rebuildNotifier(config, configJSON);
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
                    INotifierConfig configClass = ((INotifierConfig) applicationContext.getBean("notifierConfig-" + notifier.getType()));
                    INotifierConfig notifierConfig = JSON.parseObject(JSON.toJSONString(notifierJSON.get("config")), configClass.getClass());
                    notifier.setConfig(notifierConfig);
                }
            }
        }
    }

    public void save() {
        if (config != null) {
            String configString = JSON.toJSONString(config, true);
            try {
                FileUtils.writeStringToFile(new File(CONFIG_NAME), configString);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
