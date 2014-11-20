package com.piotrglazar.nightowl.configuration;

import com.piotrglazar.nightowl.coordinates.Latitude;
import com.piotrglazar.nightowl.coordinates.Longitude;
import com.piotrglazar.nightowl.logic.StarPositionProvider;
import com.piotrglazar.nightowl.model.RuntimeConfigurationRepository;
import com.piotrglazar.nightowl.model.SkyObjectVisibilitySettingsRepository;
import com.piotrglazar.nightowl.model.UserLocationRepository;
import com.piotrglazar.nightowl.model.entities.RuntimeConfiguration;
import com.piotrglazar.nightowl.model.entities.SkyObjectVisibilitySettings;
import com.piotrglazar.nightowl.model.entities.UserLocation;
import org.hsqldb.jdbc.JDBCDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
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

@Configuration
@EnableJpaRepositories(basePackages = "com.piotrglazar.nightowl")
@EnableTransactionManagement
@Profile({"default", "importing"})
public class DatabaseConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Bean
    public DataSource dataSource(DatabaseLocation databaseLocation) {
        final String databaseFileLocation = databaseLocation.getDatabaseLocation();

        LOG.info("Using database in {}", databaseFileLocation);

        return new SimpleDriverDataSource(new JDBCDriver(), "jdbc:hsqldb:file:" + databaseFileLocation, "sa", "");
    }

    @Bean
    @Autowired
    @DependsOn("databaseFromScriptReader")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final DataSource dataSource,
                                                                       final JpaVendorAdapter jpaVendorAdapter) {
        final LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource);
        bean.setJpaVendorAdapter(jpaVendorAdapter);
        bean.setPackagesToScan("com.piotrglazar.nightowl");
        return bean;
    }

    @Bean
    @DependsOn("databaseFromScriptReader")
    public JpaVendorAdapter jpaVendorAdapter() {
        final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setShowSql(true);
        jpaVendorAdapter.setGenerateDdl(true);
        jpaVendorAdapter.setDatabase(Database.HSQL);
        return jpaVendorAdapter;
    }

    @Bean
    @DependsOn("databaseFromScriptReader")
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "databasePopulator")
    @Autowired
    public DatabasePopulator databasePopulator(UserLocationRepository userLocationRepository,
                                               RuntimeConfigurationRepository runtimeConfigurationRepository,
                                               SkyObjectVisibilitySettingsRepository skyObjectVisibilitySettingsRepository) {
        return new DatabasePopulator(userLocationRepository, runtimeConfigurationRepository, skyObjectVisibilitySettingsRepository);
    }

    static class DatabasePopulator {

        private final UserLocationRepository userLocationRepository;
        private final RuntimeConfigurationRepository runtimeConfigurationRepository;
        private final SkyObjectVisibilitySettingsRepository skyObjectVisibilitySettingsRepository;

        public DatabasePopulator(UserLocationRepository userLocation, RuntimeConfigurationRepository runtimeConfiguration,
                                 SkyObjectVisibilitySettingsRepository skyObjectVisibilitySettingsRepository) {
            this.userLocationRepository = userLocation;
            this.runtimeConfigurationRepository = runtimeConfiguration;
            this.skyObjectVisibilitySettingsRepository = skyObjectVisibilitySettingsRepository;
        }

        @PostConstruct
        public void prepareDatabase() {
            LOG.info("Prepopulating database");

            final UserLocation preferredUserLocation = prepareCapitals();
            final SkyObjectVisibilitySettings skyObjectVisibilitySettings = prepareSkyObjectsVisibility();
            prepareRuntimeConfiguration(preferredUserLocation, skyObjectVisibilitySettings);

            LOG.info("Done prepopulating database");
        }

        private void prepareRuntimeConfiguration(UserLocation preferredUserLocation,
                                                 SkyObjectVisibilitySettings skyObjectVisibilitySettings) {
            if (runtimeConfigurationRepository.count() == 0) {
                runtimeConfigurationRepository.saveAndFlush(defaultRuntimeConfiguration(preferredUserLocation, skyObjectVisibilitySettings));
            }
        }

        private SkyObjectVisibilitySettings prepareSkyObjectsVisibility() {
            if (skyObjectVisibilitySettingsRepository.count() == 0) {
                SkyObjectVisibilitySettings skyObjectVisibilitySettings = new SkyObjectVisibilitySettings();
                skyObjectVisibilitySettings.setStarVisibilityMag(StarPositionProvider.BRIGHT_STAR_MAGNITUDE);
                skyObjectVisibilitySettings.setShowStarLabels(true);
                return skyObjectVisibilitySettingsRepository.saveAndFlush(skyObjectVisibilitySettings);
            } else {
                return skyObjectVisibilitySettingsRepository.findAll().get(0);
            }
        }

        private UserLocation prepareCapitals() {
            if (userLocationRepository.count() == 0) {
                LOG.info("Prepopulating database with capitals");

                final UserLocation warsaw = warsaw();
                userLocationRepository.save(warsaw);
                userLocationRepository.save(london());
                userLocationRepository.save(sydney());
                userLocationRepository.flush();

                return warsaw;
            } else {
                return userLocationRepository.findAll().get(0);
            }
        }

        private RuntimeConfiguration defaultRuntimeConfiguration(UserLocation defaultLocation, SkyObjectVisibilitySettings
                skyObjectVisibilitySettings) {
            final RuntimeConfiguration runtimeConfiguration = new RuntimeConfiguration();
            runtimeConfiguration.setChosenUserLocation(defaultLocation);
            runtimeConfiguration.setVisibilitySettings(skyObjectVisibilitySettings);
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
