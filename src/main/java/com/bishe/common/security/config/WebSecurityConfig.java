package com.bishe.common.security.config;

import com.bishe.common.security.interceptor.SecurityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器
 *
 * @author chentay
 * @date 2019/01/16
 */
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    @Bean
    public SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(securityInterceptor());
        //  排除配置--对下面的URL不进行拦截
        registration.excludePathPatterns("/**/user/login");
        registration.excludePathPatterns("/**/user/register");
        registration.excludePathPatterns("/swagger-ui.html");

        //  配置拦截
        registration.addPathPatterns("/**/user/**");
    }
}
