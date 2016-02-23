package monitorx.config;

import monitorx.filter.WebIntercepter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 添加spring mvc拦截器
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public WebIntercepter getWebMVCInterceptor() {
        return new WebIntercepter();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getWebMVCInterceptor()).addPathPatterns("/**");
    }
}