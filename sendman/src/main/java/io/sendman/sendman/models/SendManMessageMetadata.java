package io.sendman.sendman.models;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SendManMessageMetadata {

	public static final String BUNDLE_EXTRA_IDENTIFIER = SendManMessageMetadata.class.getSimpleName();
	private static final String TAG = BUNDLE_EXTRA_IDENTIFIER;

	private String title;
	private String body;

	private String activityId;
	private String messageId;

	private long deserializationTimestamp = System.currentTimeMillis();

	// TODO: consider nested structure
	private String categoryDescription;
	private String categoryName;
	private String categoryId;

	private String parentCategoryDescription;
	private String parentCategoryName;
	private String parentCategoryId;

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public String getActivityId() {
		return activityId;
	}

	public String getMessageId() {
		return messageId;
	}

	// TODO: null values
	public String getCategoryDescription() {
		return categoryDescription != null ? categoryDescription : "";
	}

	public String getCategoryName() {
		return categoryName != null ? categoryName : "Uncategorized";
	}

	public String getCategoryId() {
		return categoryId != null ? categoryId : "11111111-1111-1111-1111-111111111111";
	}

	public long getDeserializationTimestamp() {
		return deserializationTimestamp;
	}

	public static SendManMessageMetadata fromJson(JSONObject messageMetadata) {
		SendManMessageMetadata metadata = new SendManMessageMetadata();
		try {
			metadata.title = messageMetadata.getString("title");
			metadata.body = messageMetadata.getString("body");
			metadata.activityId = messageMetadata.getString("activityId");
			metadata.messageId = messageMetadata.getString("messageId");
			metadata.categoryDescription = messageMetadata.optString("categoryDescription");
			metadata.categoryName = messageMetadata.optString("categoryName");
			metadata.categoryId = messageMetadata.optString("categoryId");
			metadata.deserializationTimestamp = System.currentTimeMillis();
		} catch (JSONException e) {
			Log.w(TAG, "Cannot construct a metadata object when activityId or messageId are null.", e);
			return null;
		}
		return metadata;
	}

	public static SendManMessageMetadata fromData(Map<String, ?> data) {
		return fromJson(new JSONObject(data));
	}

	public static SendManMessageMetadata fromIntent(@NotNull Intent intent, String packageName) {
		Bundle extras = intent.getExtras();

		if (extras == null) {
			return null;
		}

		SendManMessageMetadata metadata = new Gson().fromJson(intent.getStringExtra(BUNDLE_EXTRA_IDENTIFIER), SendManMessageMetadata.class);
		metadata.deserializationTimestamp = System.currentTimeMillis();
		return metadata;
	}

}
