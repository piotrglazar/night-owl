package com.piotrglazar.nightowl.configuration;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public PropertiesFactoryBean configProperties() {
        // LOG.info("Loading config properties from file: {}", propertiesFileName);

        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("application.properties"));

        return propertiesFactoryBean;
    }
}
