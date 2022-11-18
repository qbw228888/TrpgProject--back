package com.halden.TRPG.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UploadConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = System.getProperty("user.dir") + "/static/";
        registry.addResourceHandler("/file/**")
                .addResourceLocations("file:" + path);
    }

    @Bean
    public String staticPath(){
        return System.getProperty("user.dir") + "/static/";
    }
}
