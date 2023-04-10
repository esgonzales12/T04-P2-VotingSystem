package org.teamfour.dao;

import org.teamfour.logging.LogBase;

public class BallotDao extends LogBase {

    private final String databaseName;

    public BallotDao(String databaseName) {
        super(BallotDao.class.getName() + ":" + databaseName);
        this.databaseName = databaseName;
    }



}
