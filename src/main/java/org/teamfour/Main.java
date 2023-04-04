package org.teamfour;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.teamfour.model.Election;
import org.teamfour.model.ElectionItem;
import org.teamfour.model.ElectionOffice;
import org.teamfour.system.SystemFiles;
import org.teamfour.system.data.CipherData;
import org.teamfour.system.data.Metadata;
import org.teamfour.util.JsonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String electionJson = getJsonString("src/main/resources/config/election.json");
        String officeJson = getJsonString("src/main/resources/config/election_offices.json");
        String itemJson = getJsonString("src/main/resources/config/election_items.json");
        Election election = new Gson().fromJson(electionJson, Election.class);

        ElectionOffice [] offices = new Gson().fromJson(officeJson, ElectionOffice[].class);
        ElectionItem [] items = new Gson().fromJson(itemJson, ElectionItem[].class);

        Logger log = LogManager.getLogger();
        File f = new File("alt.json");
        log.info(f.exists());
        log.info(SystemFiles.CIPHER);
        log.info(SystemFiles.LOG);
        log.info(SystemFiles.META);
        log.info(SystemFiles.SQL_STORE_PATH);
        log.info(SystemFiles.STORE_PATH);
        CipherData cipherData = JsonUtil.getJsonObj(CipherData.class, SystemFiles.CIPHER);
        Metadata metadata = JsonUtil.getJsonObj(Metadata.class, SystemFiles.META);
        log.info(cipherData.toString());
        log.info(metadata.toString());
    }

    public static void querySqlite() {
        try{
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:/" + new File(Paths.get("voting_system.db").toUri()).getAbsolutePath();
            String sql = "SELECT * FROM election";
            Connection connection = DriverManager.getConnection(url);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static String getJsonString(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (Scanner sc = new Scanner(new File(Paths.get(filePath).toUri()))) {
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}