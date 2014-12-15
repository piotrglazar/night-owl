package com.piotrglazar.nightowl.dbscript;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = {"com.piotrglazar.nightowl.dbscript", "com.piotrglazar.nightowl.util.wrappers"})
@Profile("dbScript")
public class DatabaseScriptConfiguration {

}
