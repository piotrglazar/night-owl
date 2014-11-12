package com.piotrglazar.nightowl;

import com.piotrglazar.nightowl.configuration.ApplicationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class NightOwl {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserInterface userInterface;

    @Autowired
    public NightOwl(final UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public static void main(String[] args) {
        LOG.info("Starting NightOwl");

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        applicationContext.registerShutdownHook();

        applicationContext.getBean(NightOwl.class).run();
    }

    protected void run() {
        userInterface.runUserInterface();
    }
}
