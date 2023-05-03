package org.teamfour;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.teamfour.dao.VotingDao;
import org.teamfour.dao.annotations.PrimaryKey;
import org.teamfour.dao.annotations.Table;
import org.teamfour.model.bsl.Ballot;
import org.teamfour.registry.dao.RegistryDao;
import org.teamfour.registry.data.RegisteredVoter;
import org.teamfour.registry.data.Registry;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String name = "John Doe III";
        String address = "1909 Wilkins ln NW";
        String demoHash = hash(name + address);
//        RegisteredVoter voter = new RegisteredVoter.Builder()
//                .setName(name)
//                .setAddress(address)
//                .setDemographicHash(demoHash)
//                .setVoteStatus(Registry.VoteStatus.NOT_VOTED)
//                .createRegisteredVoter();

        RegistryDao dao = new RegistryDao();
//        RegisteredVoter voter1 = dao.create(voter);
//        System.out.println("Saved: ");
//        System.out.println(voter1.toString());

        Optional<RegisteredVoter> voter2 = dao.find(demoHash);
        System.out.println("Fetched: ");
        System.out.println(voter2.isPresent());
        voter2.ifPresent(System.out::println);

        List<RegisteredVoter> voterList = dao.getVoters();
        voterList.forEach(System.out::println);

        System.exit(0);


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

    public static String hash(String input) {
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return hexString.toString();
    }
}