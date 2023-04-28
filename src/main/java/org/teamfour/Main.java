package org.teamfour;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.teamfour.dao.VotingDao;
import org.teamfour.model.bsl.Ballot;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String path = "src/main/resources/sample/ballot.json";
        VotingDao votingDao = new VotingDao();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            Ballot ballot = gson.fromJson(br, Ballot.class);
            votingDao.saveBallot(ballot);
            System.out.println(ballot);
        }  catch (IOException ignored) {}

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