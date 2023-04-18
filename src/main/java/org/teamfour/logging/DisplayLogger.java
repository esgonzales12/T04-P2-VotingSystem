package org.teamfour.logging;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface DisplayLogger {
    Logger log = LogManager.getLogger();
}
