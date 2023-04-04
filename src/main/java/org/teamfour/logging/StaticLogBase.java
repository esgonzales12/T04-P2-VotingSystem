package org.teamfour.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class StaticLogBase {
    protected static Logger log = LogManager.getLogger();
}
