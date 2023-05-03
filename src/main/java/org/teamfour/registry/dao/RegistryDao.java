package org.teamfour.registry.dao;

import org.teamfour.dao.DaoBase;
import org.teamfour.registry.data.RegisteredVoter;
import org.teamfour.system.SystemFiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RegistryDao extends DaoBase {

    private final String DB_FILE = "registeredVoters.db";
    private final String TABLE_NAME = "registeredVoter";

    public RegistryDao() {}

    @Override
    public Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(SystemFiles.REGISTRY_DB_PATH + DB_FILE);
        } catch (SQLException e) {
            log.error("UNABLE TO OPEN CONNECTION");
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<RegisteredVoter> find(String demoHash) {
        String sql = String.format(
                """
                SELECT * FROM registeredVoter
                WHERE registeredVoter.demographicHash = '%s';
                """, demoHash);
        return selectOne(RegisteredVoter.class, sql);
    }

    public boolean update(String demoHash, String status) {
        String sql = String.format(
                """
                UPDATE %s
                SET registeredVoter.voteStatus = %s
                WHERE registeredVoter.demographicHash = %s;
                """, TABLE_NAME, status, demoHash);
        int result = update(sql);
        if (result != 1) {
            log.error("ROWS AFFECTED = {} != 1 FOR HASH {} AND STATUS {}", result, demoHash, status);
        }
        return result == 1;
    }

    public RegisteredVoter create(RegisteredVoter registeredVoter) {
        String sql = String.format(
                """
                INSERT INTO registeredVoter
                (demographicHash, name, address, voteStatus)
                VALUES ('%s', '%s', '%s', '%s');
                """, registeredVoter.getDemographicHash(), registeredVoter.getName(),
                registeredVoter.getAddress(), registeredVoter.getVoteStatus());
        return create(RegisteredVoter.class, sql);
    }

    public List<RegisteredVoter> getVoters() {
        String sql =
                """
                SELECT * FROM registeredVoter;
                """;
        return selectMany(RegisteredVoter.class, sql);
    }

}
