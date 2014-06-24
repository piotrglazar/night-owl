package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.RuntimeConfigurationRepository;
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

@Configuration
@EnableJpaRepositories(basePackages = "com.piotrglazar.nightowl")
@EnableTransactionManagement
@Profile("default")
public class DatabaseConfiguration {

    @Bean
    public DataSource dataSource(){
        return new SimpleDriverDataSource(new JDBCDriver(), "jdbc:hsqldb:file:/home/nightowl/production", "sa", "");
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource,
                                                                       final JpaVendorAdapter jpaVendorAdapter) {
        final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setPackagesToScan("com.piotrglazar.nightowl");
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

    @Bean(name = "databasePopulator")
    @Autowired
    public DatabasePopulator databasePopulator(UserLocationRepository userLocationRepository,
                                               RuntimeConfigurationRepository runtimeConfigurationRepository) {
        return new DatabasePopulator(userLocationRepository, runtimeConfigurationRepository);
    }

    static class DatabasePopulator {

        private final UserLocationRepository userLocationRepository;
        private final RuntimeConfigurationRepository runtimeConfigurationRepository;

        public DatabasePopulator(UserLocationRepository userLocation, RuntimeConfigurationRepository runtimeConfiguration) {
            this.userLocationRepository = userLocation;
            this.runtimeConfigurationRepository = runtimeConfiguration;
        }

        @PostConstruct
        public void prepareDatabase() {
            prepareCapitals();
        }

        private void prepareCapitals() {
            if (userLocationRepository.count() == 0) {
                final UserLocation warsaw = warsaw();
                userLocationRepository.save(warsaw);
                userLocationRepository.save(london());
                userLocationRepository.save(sydney());
                userLocationRepository.flush();

                runtimeConfigurationRepository.saveAndFlush(defaultRuntimeConfiguration(warsaw));
            }
        }

        private RuntimeConfiguration defaultRuntimeConfiguration(final UserLocation defaultLocation) {
            final RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration();
            runtimeConfiguration.setChosenUserLocation(defaultLocation);
            return runtimeConfiguration;
        }

        private UserLocation sydney() {
            return UserLocation.builder()
                    .latitude(new Latitude(-34.0))
                    .longitude(new Longitude(151.0))
                    .name("Sydney")
                    .build();
        }

        private UserLocation london() {
            return UserLocation.builder()
                    .latitude(new Latitude(51.51))
                    .longitude(new Longitude(-0.13))
                    .name("London")
                    .build();
        }

        private UserLocation warsaw() {
            return UserLocation.builder()
                    .latitude(new Latitude(52.23))
                    .longitude(new Longitude(21.0))
                    .name("Warsaw")
                    .build();
        }
    }
}
