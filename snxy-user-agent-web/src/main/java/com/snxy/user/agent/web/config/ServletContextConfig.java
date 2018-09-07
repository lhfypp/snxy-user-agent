package com.snxy.user.agent.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * Created by zcb on 2017/5/16.
 */
@Configuration
public class ServletContextConfig extends WebMvcConfigurationSupport {

   /* @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("配置拦截器映射");

        registry.addResourceHandler("*//**").addResourceLocations("classpath:/static/");
    }
*/

    @Override
    protected void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }

}
