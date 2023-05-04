package org.teamfour;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.teamfour.dao.VotingDao;
import org.teamfour.model.bsl.Ballot;
import org.teamfour.registry.dao.RegistryDao;
import org.teamfour.registry.data.RegisteredVoter;
import org.teamfour.registry.data.Registry;
import org.teamfour.system.data.Claims;
import org.teamfour.system.data.DeviceFiles;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.data.SystemFiles;
import org.teamfour.system.enums.Authority;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

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
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String path = "src/main/resources/sample/ballot.json";
        VotingDao votingDao = new VotingDao();
        try (BufferedReader br = new BufferedReader(new FileReader(SystemFiles.DEVICE_PATH + DeviceFiles.BALLOT))) {
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