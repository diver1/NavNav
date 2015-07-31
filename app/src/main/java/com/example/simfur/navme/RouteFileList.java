package com.example.simfur.navme;

import android.app.Activity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteFileList {
    public static List<RouteFile> ITEMS = new ArrayList<>();
    public static Map<String, RouteFile> ITEM_MAP = new HashMap<>();

    private static void addItem(RouteFile item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void populate(Activity a) {
        String [] fileList = null;
        // Create a list of all files in assets/routes
        try {
            fileList = a.getAssets().list("routes");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (fileList != null) {
            for ( int i = 0;i<fileList.length;i++) {
                RouteFileList.addItem(new RouteFile(Integer.toString(i), fileList[i]));
            }
        }
    }

    public static class RouteFile {
        public String id;
        public String content;

        public RouteFile(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
