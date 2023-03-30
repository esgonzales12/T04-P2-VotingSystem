package org.teamfour;

import com.google.gson.Gson;
import org.teamfour.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String electionJson = getJsonString("src/main/resources/config/election.json");
        String officeJson = getJsonString("src/main/resources/config/election_offices.json");
        String itemJson = getJsonString("src/main/resources/config/election_items.json");
        Election election = new Gson().fromJson(electionJson, Election.class);

        ElectionOffice [] offices = new Gson().fromJson(officeJson, ElectionOffice[].class);
        ElectionItem [] items = new Gson().fromJson(itemJson, ElectionItem[].class);
        for (ElectionOffice office: offices) {
            System.out.println(office.toString());
        }

        for (ElectionItem item: items) {
            System.out.println(item.toString());
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