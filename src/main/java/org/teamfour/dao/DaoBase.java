package org.teamfour.dao;

import org.teamfour.logging.LogBase;
import org.teamfour.system.SystemFiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DaoBase extends LogBase {
    public DaoBase(String logIdentifier) {
        super(logIdentifier);
    }

    private Connection getConnection(String databaseName) {
        String url = SystemFiles.SQL_STORE_PATH + databaseName;
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            log.error("UNABLE TO GET DATABASE CONNECTION FOR URL:");
            log.error(url);
            log.error(e.getMessage());
            throw new RuntimeException("SQL EXCEPTION");
        }
    }
}
