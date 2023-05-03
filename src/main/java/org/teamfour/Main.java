package org.teamfour;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.teamfour.dao.VotingDao;
import org.teamfour.model.bsl.Ballot;
import org.teamfour.registry.dao.RegistryDao;
import org.teamfour.registry.data.RegisteredVoter;
import org.teamfour.registry.data.Registry;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.data.SystemFiles;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String[] NAMES = {"Emily Garcia", "Julian Patel", "Hannah Nguyen", "Owen Lee", "Eva Kim", "Isabella Chen", "Aiden Smith", "Avery Taylor", "Mason Wong", "Sophia Rodriguez"};
    private static final String[] ADDRESSES = {"123 Main St", "456 Oak Ave", "789 Maple Rd", "1011 Elm Blvd", "1213 Cedar Ln", "1415 Pine Dr", "1617 Birch Rd", "1819 Ash Ave", "2021 Walnut St", "2223 Chestnut Blvd"};


    private static Metadata fetchSystemData() {
        try (BufferedReader br = new BufferedReader(new FileReader(SystemFiles.META))) {
            // TODO: service.saveBallot(ballot);
            return new Gson().fromJson(br, Metadata.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("UNABLE TO READ SYSTEM METADATA");
        }
        return null;
    }

    public static void main(String[] args) {
        Metadata metadata = fetchSystemData();
        System.out.println(metadata.toString());
        System.exit(0);
        RegistryDao dao = new RegistryDao();
        System.out.println(SystemFiles.REGISTRY_DB_PATH);
        for (int i = 0; i < NAMES.length; i++) {
            RegisteredVoter voter = new RegisteredVoter.Builder()
                .setName(NAMES[i])
                .setAddress(ADDRESSES[i])
                .setDemographicHash(hash(NAMES[i] + ADDRESSES[i]).substring(0, 6).toUpperCase())
                .setVoteStatus(Registry.VoteStatus.NOT_VOTED)
                .createRegisteredVoter();
            dao.create(voter);
        }

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