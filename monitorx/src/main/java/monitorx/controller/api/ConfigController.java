package monitorx.controller.api;

import monitorx.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qianlifeng
 */
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    ConfigService configService;

    @RequestMapping("/title")
    public APIResponse getTitle() {
        return APIResponse.buildSuccessResponse(configService.getConfig().getApp().getTitle());
    }
}
