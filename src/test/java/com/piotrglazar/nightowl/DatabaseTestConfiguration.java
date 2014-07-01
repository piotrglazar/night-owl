package com.piotrglazar.nightowl;

import com.google.common.collect.ImmutableMap;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.RuntimeConfigurationRepository;
import com.piotrglazar.nightowl.model.StarInfo;
import com.piotrglazar.nightowl.model.StarInfoRepository;
import com.piotrglazar.nightowl.model.UserLocation;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.LocalTime;

@Configuration
@EnableJpaRepositories(basePackages = "com.piotrglazar.nightowl")
@EnableTransactionManagement
@Profile("test")
public class DatabaseTestConfiguration {

    @Bean
    public DataSource dataSource() {
        return new SimpleDriverDataSource(new JDBCDriver(), "jdbc:hsqldb:file:/home/nightowl/test", "sa", "");
    }

    @Bean
    @Autowired
    public DatabasePopulator databasePopulator(StarInfoRepository starInfoRepository, UserLocationRepository userLocationRepository,
            RuntimeConfigurationRepository runtimeConfigurationRepository) {
        return new DatabasePopulator(userLocationRepository, runtimeConfigurationRepository, starInfoRepository);
    }

    static class DatabasePopulator {

        private final UserLocationRepository userLocationRepository;
        private final RuntimeConfigurationRepository runtimeConfigurationRepository;
        private final StarInfoRepository starInfoRepository;

        public DatabasePopulator(UserLocationRepository userLocationRepository,
                                 RuntimeConfigurationRepository runtimeConfigurationRepository,
                                 StarInfoRepository starInfoRepository) {
            this.userLocationRepository = userLocationRepository;
            this.runtimeConfigurationRepository = runtimeConfigurationRepository;
            this.starInfoRepository = starInfoRepository;
        }

        @PostConstruct
        public void fillTestDatabase() {
            saveSomeStarInfo();
            prepareUserLocalisation();
        }

        private void prepareUserLocalisation() {
            final UserLocation warsaw = warsaw();
            userLocationRepository.saveAndFlush(warsaw);

            runtimeConfigurationRepository.saveAndFlush(defaultRuntimeConfiguration(warsaw));
        }

        private void saveSomeStarInfo() {
            starInfoRepository.saveAndFlush(new StarInfo(LocalTime.now().plusSeconds(10), 82.71, "A0"));
            starInfoRepository.saveAndFlush(new StarInfo(LocalTime.now().plusSeconds(50), 73.13, "B0"));
            starInfoRepository.saveAndFlush(new StarInfo(LocalTime.now().plusSeconds(100), 52.92, "C0"));
            starInfoRepository.saveAndFlush(new StarInfo(LocalTime.now().plusSeconds(150), 22.42, "D0"));
            starInfoRepository.saveAndFlush(new StarInfo(LocalTime.now().plusSeconds(200), -23.65, "E0"));
            starInfoRepository.saveAndFlush(new StarInfo(LocalTime.now().plusSeconds(250), -55.22, "F0"));
        }

        private RuntimeConfiguration defaultRuntimeConfiguration(final UserLocation defaultLocation) {
            final RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration();
            runtimeConfiguration.setChosenUserLocation(defaultLocation);
            return runtimeConfiguration;
        }

        private UserLocation warsaw() {
            return UserLocation.builder()
                    .latitude(new Latitude(52.23))
                    .longitude(new Longitude(21.0))
                    .name("Warsaw")
                    .build();
        }
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource,
                                                                       final JpaVendorAdapter jpaVendorAdapter) {
        final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setPackagesToScan("com.piotrglazar.nightowl");
        bean.setJpaPropertyMap(ImmutableMap.of("hibernate.hbm2ddl.auto", "create"));
        return bean;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setDatabase(Database.HSQL);
        return jpaVendorAdapter;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
