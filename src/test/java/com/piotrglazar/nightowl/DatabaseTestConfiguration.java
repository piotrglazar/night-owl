package com.piotrglazar.nightowl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.piotrglazar.nightowl.configuration.ApplicationConfiguration;
import com.piotrglazar.nightowl.model.entities.StarColor;
import com.piotrglazar.nightowl.model.entities.StarInfo;
import com.piotrglazar.nightowl.model.entities.StarInfoDetails;
import org.hsqldb.jdbc.JDBCDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static StarInfoDetails name(String name) {
        return new StarInfoDetails(name);
    }

    public static final List<StarInfo> STARS = ImmutableList.of(
            new StarInfo(LocalTime.of(23, 36, 32), 77.46, "O", name("Alpha"), -2.0, StarColor.O),
            new StarInfo(LocalTime.of(0, 59, 10), 73.17, "B", name("Beta"), -1.0, StarColor.B),
            new StarInfo(LocalTime.of(4, 45, 18), 53.17, "A", name("Gamma"), 0.0, StarColor.A),
            new StarInfo(LocalTime.of(11, 45, 18), 22.41, "F", name("Delta"), 1.0, StarColor.F),
            new StarInfo(LocalTime.of(15, 12, 50), -23.65, "G", name("Epsilon"), 2.0, StarColor.G),
            new StarInfo(LocalTime.of(22, 10, 12), -55.22, "K", name("Zeta"), 3.0, StarColor.K));

    @Bean
    public DataSource dataSource() {
        final String currentDirectory = ApplicationConfiguration.getCurrentDirectory();

        LOG.info("Creating test database in {}/database/test", currentDirectory);

        return new SimpleDriverDataSource(new JDBCDriver(), "jdbc:hsqldb:file:" + currentDirectory + "/database/test", "sa", "");
    }

    @Bean
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
