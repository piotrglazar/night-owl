package com.piotrglazar.nightowl.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.lang.invoke.MethodHandles;

@Configuration
public class NightOwlProperties {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public PropertiesFactoryBean configProperties() {
        LOG.info("Loading config properties from file: application.properties");

        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("application.properties"));

        return propertiesFactoryBean;
    }
}
