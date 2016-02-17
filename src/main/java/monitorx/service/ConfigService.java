package monitorx.service;

import com.alibaba.fastjson.JSON;
import monitorx.domain.config.Config;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class ConfigService {

    static String CONFIG_NAME = "config.json";
    static Config config;

    public Config getConfig() {
        if (config != null) return config;

        File file = new File(CONFIG_NAME);
        if (file.exists()) {
            try {
                config = JSON.parseObject(FileUtils.readFileToString(file), Config.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //load default config
            config = JSON.parseObject(JSON.toJSONString(new Config()), Config.class);
        }

        return config;
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
