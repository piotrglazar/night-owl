package com.piotrglazar.nightowl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class NightOwl {

    private final UserInterface userInterface;

    @Autowired
    public NightOwl(final UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public static void main(String[] args) {
        final ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        applicationContext.registerShutdownHook();

        applicationContext.getBean(NightOwl.class).run();
    }

    protected void run() {
        userInterface.runUserInterface();
    }
}
