package com.piotrglazar.nightowl.configuration;

import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@EnableCaching
public class ApplicationConfiguration {

    public static final String NIGHT_OWL_CACHE = "nightOwlCache";

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("#{configProperties['star.position.provider.cache.expires.in.seconds']?:240}")
    private int starPositionProviderCache;

    @Bean
    public PropertiesFactoryBean configProperties() {
        LOG.info("Loading config properties from file: application.properties");

        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("application.properties"));

        return propertiesFactoryBean;
    }

    @Bean
    public GuavaCacheManager guavaCacheManager() {
        final GuavaCacheManager nightOwlCache = new GuavaCacheManager(NIGHT_OWL_CACHE);
        final CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        cacheBuilder.expireAfterWrite(starPositionProviderCache, TimeUnit.SECONDS);
        nightOwlCache.setCacheBuilder(cacheBuilder);
        return nightOwlCache;
    }

    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
}
