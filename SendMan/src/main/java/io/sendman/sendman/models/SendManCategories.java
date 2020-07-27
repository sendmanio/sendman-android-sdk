package io.sendman.sendman.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SendManCategories {
    public static ArrayList<SendManCategory> fromJson(JSONArray categoriesJson) {
        JSONObject categoryJson;
        ArrayList<SendManCategory> categories = new ArrayList<>(categoriesJson.length());
        for (int i=0; i < categoriesJson.length(); i++) {
            try {
                categoryJson = categoriesJson.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            SendManCategory category = SendManCategory.fromJson(categoryJson);
            if (category != null) {
                categories.add(category);
            }
        }

        return categories;
    }

    public static JSONArray toJson(ArrayList<SendManCategory> categories) {
        SendManCategory category;
        JSONArray categoriesJson = new JSONArray();
        for (int i=0; i < categories.size(); i++) {
            category = categories.get(i);
            JSONObject categoryJson = SendManCategory.toJson(category);
            if (categoryJson != null) {
                categoriesJson.put(categoryJson);
            }
        }

        return categoriesJson;
    }
}


