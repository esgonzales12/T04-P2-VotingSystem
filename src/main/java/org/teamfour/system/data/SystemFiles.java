package org.teamfour.system.data;

import java.io.File;

import static java.util.Objects.isNull;

public class SystemFiles {
    private static final String USER_DIR = "user.dir";
    private static final String SYSTEM_DIR = "sys";
    private static final String SYSTEM_DATA = "data";
    private static final String SYSTEM_STORE = "store";

    public static final String DEVICE_PATH = new PathBuilder(File.separator)
            .withExtension("device")
            .withExtension("")
            .build();

    public static final String STORE_PATH = new PathBuilder(File.separator)
            .withExtension(SYSTEM_DIR)
            .withExtension("store")
            .withExtension("")
            .build();
    public static final String META = new PathBuilder(File.separator)
            .withExtension(System.getProperty(USER_DIR))
            .withExtension(SYSTEM_DIR)
            .withExtension(SYSTEM_DATA)
            .withExtension("meta.json")
            .build();
    public static final String LOG = new PathBuilder(File.separator)
            .withExtension(System.getProperty(USER_DIR))
            .withExtension(SYSTEM_DIR)
            .withExtension(SYSTEM_DATA)
            .withExtension("system.log")
            .build();
    public static final String CIPHER = new PathBuilder(File.separator)
            .withExtension(System.getProperty(USER_DIR))
            .withExtension(SYSTEM_DIR)
            .withExtension(SYSTEM_DATA)
            .withExtension("cipher.json")
            .build();
    public static final String SQL_STORE_PATH = new PathBuilder(File.separator)
            .withExtension(new PathBuilder(":")
                            .withExtension("jdbc")
                            .withExtension("sqlite")
                            .withExtension("")
                            .build())
            .withExtension(System.getProperty(USER_DIR))
            .withExtension(SYSTEM_DIR)
            .withExtension(SYSTEM_STORE)
            .withExtension("")
            .build();

    public static final String REGISTRY_DB_PATH = new PathBuilder(File.separator)
            .withExtension(new PathBuilder(":")
                    .withExtension("jdbc")
                    .withExtension("sqlite")
                    .withExtension("")
                    .build())
            .withExtension(System.getProperty(USER_DIR))
            .withExtension(SYSTEM_DIR)
            .withExtension("registry")
            .withExtension("")
            .build();

    private static class PathBuilder {
        private String path;
        private final String delim;

        public PathBuilder(String delimiter) {
            this.delim = delimiter;
        }

        public PathBuilder withExtension(String extension) {
            this.path = isNull(path) ? extension : path + delim + extension;
            return this;
        }

        public String build() {
            return this.path;
        }
    }
}
