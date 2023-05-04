package org.teamfour;

import com.google.gson.Gson;
import org.teamfour.system.data.Metadata;
import org.teamfour.system.data.SystemFiles;
import org.teamfour.system.enums.Status;

import java.io.BufferedReader;
import java.io.FileReader;

public class Test {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader(SystemFiles.META))) {
            Metadata metadata = new Gson().fromJson(br, Metadata.class);
            metadata.setStatus(Status.PRE_ELECTION);
            System.out.println(new Gson().toJson(metadata));
            System.out.println(metadata);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
