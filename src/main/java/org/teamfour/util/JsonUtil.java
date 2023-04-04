package org.teamfour.util;

import com.google.gson.Gson;
import org.teamfour.logging.StaticLogBase;

import java.io.*;
import java.util.Scanner;

public class JsonUtil extends StaticLogBase {

    public static String parseJsonString(String filePath) {
        StringBuilder sb = new StringBuilder();
        String path = System.getProperty("user.dir") + "/" +  filePath;
        try (Scanner sc = new Scanner(new File(path))) {
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            log.warn("UNABLE TO LOCATE FILE AT {}", path);
            log.warn(e.getMessage());
        }
        return "";
    }

    public static <T> T getJsonObj(Class<T> c, String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return new Gson().fromJson(br, c);
        }  catch (IOException e) {
            log.warn("UNABLE TO LOCATE FILE AT {}", path);
            log.warn(e.getMessage());
        }
        return null;
    }
}
