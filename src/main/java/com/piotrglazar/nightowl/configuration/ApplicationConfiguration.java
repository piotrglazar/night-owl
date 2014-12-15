package com.piotrglazar.nightowl.configuration;

import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@EnableCaching
@ComponentScan(basePackages = "com.piotrglazar.nightowl",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "DatabaseConfiguration")})
public class ApplicationConfiguration {

    public static final String NIGHT_OWL_CACHE = "nightOwlCache";

    @Value("#{configProperties['star.position.provider.cache.expires.in.seconds']?:240}")
    private int starPositionProviderCache;

    @Bean
    public GuavaCacheManager guavaCacheManager() {
        final GuavaCacheManager nightOwlCache = new GuavaCacheManager(NIGHT_OWL_CACHE);
        final CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
        cacheBuilder.expireAfterWrite(starPositionProviderCache, TimeUnit.SECONDS);
        nightOwlCache.setCacheBuilder(cacheBuilder);
        return nightOwlCache;
    }

    @Bean
    public AnnotationMBeanExporter annotationMBeanExporter() {
        return new AnnotationMBeanExporter();
    }

    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
}
