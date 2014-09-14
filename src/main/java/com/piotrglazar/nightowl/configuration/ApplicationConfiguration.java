package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.ui.map.DirectionsSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableAsync
public class ApplicationConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public PropertiesFactoryBean configProperties() {
        LOG.info("Loading config properties from file: application.properties");

        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("application.properties"));

        return propertiesFactoryBean;
    }

    @Bean
    public List<DirectionsSign> directionsSigns() {
        return Arrays.asList(DirectionsSign.values());
    }

    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
}
