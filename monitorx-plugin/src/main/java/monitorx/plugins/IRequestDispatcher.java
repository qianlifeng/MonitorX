package monitorx.plugins;

import org.pf4j.ExtensionPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qianlifeng
 * implement this extesionpoint if plugin want to expose url request mapping
 */
public interface IRequestDispatcher extends ExtensionPoint {
    String getUrl();

    void handle(HttpServletRequest request, HttpServletResponse response);
}