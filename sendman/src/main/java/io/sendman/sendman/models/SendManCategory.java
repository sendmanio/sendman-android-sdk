package io.sendman.sendman.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SendManCategory implements SendManIdentifiable {

    private String id;
    private String name;
    private String description;
    private String defaultValue;
    private String value;

    private ArrayList<SendManCategory> subCategories;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getDefaultValue() {
        return defaultValue.equals("null") ? null : Boolean.valueOf(defaultValue);
    }

    public Boolean getValue() {
        return value.equals("null") ? null : Boolean.valueOf(value);
    }

    public void setValue(Boolean value) {
        this.value = String.valueOf(value);
    }

    public ArrayList<SendManCategory> getSubCategories() {
        return subCategories;
    }

    static SendManCategory fromJson(JSONObject categoryJson) {
        SendManCategory category = new SendManCategory();
        try {
            category.id = categoryJson.getString("id");
            category.name = categoryJson.optString("name");
            category.description = categoryJson.optString("description");
            category.defaultValue = categoryJson.getString("defaultValue");
            category.value = categoryJson.optString("value");

            JSONArray subCategoriesJson = categoryJson.optJSONArray("categories");
            if (subCategoriesJson != null) {
                for (int i=0; i < subCategoriesJson.length(); i++) {
                    JSONObject subCategoryJson = subCategoriesJson.getJSONObject(i);
                    SendManCategory subCategory = SendManCategory.fromJson(subCategoryJson);
                    if (subCategory != null && category.subCategories == null) {
                        category.subCategories = new ArrayList<>(subCategoriesJson.length());
                    }
                    category.subCategories.add(subCategory);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return category;
    }

    static JSONObject toJson(SendManCategory category) {
        JSONObject categoryJson = new JSONObject();
        try {
            categoryJson.put("id", category.getId());
            categoryJson.put("name", category.getName());
            categoryJson.put("description", category.getDescription());
            categoryJson.put("defaultValue", category.getDefaultValue());
            categoryJson.put("value", category.getValue());

            ArrayList<SendManCategory> subCategories = category.getSubCategories();
            if (subCategories != null && subCategories.size() > 0) {
                for (int i=0; i < subCategories.size(); i++) {
                    SendManCategory subCategory = subCategories.get(i);
                    JSONObject subCategoryJson = SendManCategory.toJson(subCategory);
                    if (subCategoryJson != null && categoryJson.optJSONArray("categories") == null) {
                        categoryJson.put("categories", new JSONArray());
                    }
                    categoryJson.getJSONArray("categories").put(subCategoryJson);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return categoryJson;
    }
}
