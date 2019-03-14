package com.ricardopassarella.nbrown.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ValidateClientIdConfig implements WebMvcConfigurer {

    @Autowired
    private ValidateClientIdInterceptor validateClientIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(validateClientIdInterceptor);
    }
}
