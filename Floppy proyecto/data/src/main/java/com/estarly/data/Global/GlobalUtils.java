package com.estarly.data.Global;


public class GlobalUtils {

    public static String[] COLLECTIONS = {
            "Users",
            "States",
            "Conversations",
            "Stickers"
    };

    public enum TypeFile{
        AUDIO("audios" ),
        IMAGE("images"),
        IMAGE_USER("");

        private String dir;

        TypeFile(String dir) {
            this.dir = dir;
        }

        public String getDir() {
            return dir;
        }
    }

}
