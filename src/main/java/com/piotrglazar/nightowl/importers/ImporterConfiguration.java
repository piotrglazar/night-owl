package com.piotrglazar.nightowl.importers;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = {"com.piotrglazar.nightowl.importers", "com.piotrglazar.nightowl.util.wrappers"})
@Profile("importing")
public class ImporterConfiguration {

}
