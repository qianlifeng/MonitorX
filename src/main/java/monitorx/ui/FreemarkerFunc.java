package monitorx.ui;

import monitorx.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FreemarkerFunc {
    @Autowired
    ConfigService configService;

    public String getAppTitle() {
        String title = configService.getConfig().getApp().getTitle();
        if (StringUtils.isEmpty(title)) title = "MonitorX";

        return title;
    }
}
