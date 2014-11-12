package com.piotrglazar.nightowl;

import javax.annotation.PostConstruct;

public interface DatabaseFromScriptReader {
    @PostConstruct
    void createDatabaseFromScript();
}
