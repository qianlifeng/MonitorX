package monitorx.filter;

import monitorx.ui.FreemarkerFunc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebIntercepter extends HandlerInterceptorAdapter {

    @Autowired
    FreemarkerFunc freemarkerFunc;

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView == null || modelAndView.getViewName().startsWith("redirect:")) return;

        addFreemarkerFunctions(modelAndView);
    }

    /**
     * 注册一些freemarker使用的functions
     */
    private void addFreemarkerFunctions(ModelAndView modelAndView) {
        modelAndView.getModel().put("func", freemarkerFunc);
    }
}