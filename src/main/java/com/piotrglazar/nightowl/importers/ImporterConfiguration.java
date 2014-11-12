package com.piotrglazar.nightowl.importers;

import com.piotrglazar.nightowl.configuration.DatabaseLocation;
import org.hsqldb.jdbc.JDBCDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = {"com.piotrglazar.nightowl.importers", "com.piotrglazar.nightowl.dbscript",
        "com.piotrglazar.nightowl.configuration", "com.piotrglazar.nightowl.model", "com.piotrglazar.nightowl.util.wrappers"})
@Profile("importing")
public class ImporterConfiguration {

    @Bean
    public DatabaseLocation databaseLocation() {
        return new DatabaseLocation();
    }

    @Bean
    public DataSource dataSource(DatabaseLocation databaseLocation) {
        return new SimpleDriverDataSource(
                new JDBCDriver(), "jdbc:hsqldb:file:" + databaseLocation.getDatabaseLocation(), "sa", "");
    }
}
