package com.piotrglazar.nightowl.api;

import javax.annotation.PostConstruct;

public interface DatabaseFromScriptReader {
    @PostConstruct
    void createDatabaseFromScript();
}
