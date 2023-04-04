package org.teamfour.logging;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LogBase {
    protected Logger log;

    public LogBase(String logIdentifier) {
        log = LogManager.getLogger(logIdentifier);
    }
}
