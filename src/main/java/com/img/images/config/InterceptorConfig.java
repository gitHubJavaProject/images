package com.img.images.config;


import com.img.images.interceptor.PermissionCheckerInterceptor;
import com.img.images.interceptor.UserLoaderInterceptor;
import com.img.images.interceptor.UserLoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserLoginCheckInterceptor()).addPathPatterns("/front/p/**", "/business/p/**");
        registry.addInterceptor(new UserLoaderInterceptor()).addPathPatterns("/front/**", "/front/p/**", "/business/p/**");
        registry.addInterceptor(new PermissionCheckerInterceptor()).addPathPatterns("/front/p/**", "/business/p/**");
    }
}
