package com.piotrglazar.nightowl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.piotrglazar.nightowl.configuration.ApplicationConfiguration;
import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.RuntimeConfigurationRepository;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.StarInfoRepository;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import org.hsqldb.jdbc.JDBCDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.lang.invoke.MethodHandles;
import java.time.LocalTime;
import java.util.List;

@Configuration
@EnableJpaRepositories(basePackages = "com.piotrglazar.nightowl")
@EnableTransactionManagement
@Profile("test")
public class DatabaseTestConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final List<StarInfo> STARS = ImmutableList.of(
            new StarInfo(LocalTime.of(23, 36, 32), 77.46, "A0", "Alpha", -2.0),
            new StarInfo(LocalTime.of(0, 59, 10), 73.17, "B0", "Beta", -1.0),
            new StarInfo(LocalTime.of(4, 45, 18), 53.17, "C0", "Gamma", 0.0),
            new StarInfo(LocalTime.of(11, 45, 18), 22.41, "D0", "Delta", 1.0),
            new StarInfo(LocalTime.of(15, 12, 50), -23.65, "E0", "Epsilon", 2.0),
            new StarInfo(LocalTime.of(22, 10, 12), -55.22, "F0", "Zeta", 3.0));

    @Bean
    public DataSource dataSource() {
        final String currentDirectory = ApplicationConfiguration.getCurrentDirectory();

        LOG.info("Creating test database in {}/database/test", currentDirectory);

        return new SimpleDriverDataSource(new JDBCDriver(), "jdbc:hsqldb:file:" + currentDirectory + "/database/test", "sa", "");
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
            starInfoRepository.save(STARS);
            starInfoRepository.flush();
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
