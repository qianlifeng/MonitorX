package monitorx.controller;

import monitorx.plugins.IRequestDispatcher;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author qianlifeng
 */
@RestController
public class PluginRequestDispatcher {

    @Autowired
    PluginManager pluginManager;

    @RequestMapping("/api/**")
    public void dispatch(HttpServletRequest request, HttpServletResponse response) {
        List<IRequestDispatcher> dispatcherList = pluginManager.getExtensions(IRequestDispatcher.class);
        String[] urlPaths = request.getRequestURL().toString().split("api");
        if (urlPaths.length > 1 && dispatcherList.size() > 0) {
            String url = urlPaths[1];
            dispatcherList.stream().filter(o -> o.getUrl().equalsIgnoreCase(url)).findFirst().ifPresent(i -> {
                i.handle(request, response);
            });
        }
    }
}