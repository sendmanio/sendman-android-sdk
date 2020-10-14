package io.sendman.sendman.models;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SendManNotificationMetadata {

	public static final String BUNDLE_EXTRA_IDENTIFIER = SendManNotificationMetadata.class.getSimpleName();
	private static final String TAG = BUNDLE_EXTRA_IDENTIFIER;

	private String title;
	private String body;

	private String activityId;
	private String templateId;

	private long deserializationTimestamp = System.currentTimeMillis();

	// TODO: consider nested structure
	private String categoryDescription;
	private String categoryName;
	private String categoryId;

	private String parentCategoryDescription;
	private String parentCategoryName;
	private String parentCategoryId;

	private String smallIconFilename;

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public String getActivityId() {
		return activityId;
	}

	public String getTemplateId() {
		return templateId;
	}

	// TODO: null values
	public String getCategoryDescription() {
		return categoryDescription != null ? categoryDescription : "";
	}

	public String getCategoryName() {
		return categoryName != null ? categoryName : "Default";
	}

	public String getCategoryId() {
		return categoryId != null ? categoryId : "11111111-1111-1111-1111-111111111111";
	}

	public long getDeserializationTimestamp() {
		return deserializationTimestamp;
	}

	public String getSmallIconFilename() {
		return smallIconFilename;
	}

	public static SendManNotificationMetadata fromJson(JSONObject notificationMetadata) {
		SendManNotificationMetadata metadata = new SendManNotificationMetadata();
		try {
			metadata.title = notificationMetadata.getString("title");
			metadata.body = notificationMetadata.getString("body");
			metadata.activityId = notificationMetadata.getString("smActivityId");
			metadata.templateId = notificationMetadata.getString("smTemplateId");
			metadata.categoryDescription = notificationMetadata.optString("smCategoryDescription", null);
			metadata.categoryName = notificationMetadata.optString("smCategoryName", null);
			metadata.categoryId = notificationMetadata.optString("smCategoryId", null);
			metadata.smallIconFilename = notificationMetadata.optString("smSmallIcon", null);
			metadata.deserializationTimestamp = System.currentTimeMillis();
		} catch (JSONException e) {
			Log.w(TAG, "Cannot construct a metadata object when activityId or templateId are null.", e);
			return null;
		}
		return metadata;
	}

	public static SendManNotificationMetadata fromData(Map<String, ?> data) {
		return fromJson(new JSONObject(data));
	}

	public static SendManNotificationMetadata fromIntent(@NonNull Intent intent, String packageName) {
		Bundle extras = intent.getExtras();

		if (extras == null) {
			return null;
		}

		SendManNotificationMetadata metadata = new Gson().fromJson(intent.getStringExtra(BUNDLE_EXTRA_IDENTIFIER), SendManNotificationMetadata.class);
		metadata.deserializationTimestamp = System.currentTimeMillis();
		return metadata;
	}

}
