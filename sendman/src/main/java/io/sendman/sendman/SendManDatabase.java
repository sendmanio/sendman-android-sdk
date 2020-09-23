package io.sendman.sendman;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

import androidx.annotation.Nullable;
import io.sendman.sendman.models.SendManSession;

public class SendManDatabase {

	private static final String SENDMAN_STORAGE = "sendman-storage";
	private static final String SENDMAN_LAST_SESSION_KEY = "sendman-last-session-key";
	private static final String SENDMAN_AUTO_USER_ID = "sendman-auto-user-id";

	/** --- Data Members --- */

	protected Context context;

	/** --- Constructor --- */

	public SendManDatabase(Context context) {
		this.context = context;
	}

	/** --- Logical storage ---- */

	@Nullable
	public SendManSession getLastSession() {
		return this.getObject(SENDMAN_STORAGE, SENDMAN_LAST_SESSION_KEY, SendManSession.class);
	}

	public void setLastSession(SendManSession session) {
		this.putObject(SENDMAN_STORAGE, SENDMAN_LAST_SESSION_KEY, session, true);
	}

	@Nullable
	public String getAutoUserId() {
		return this.getObject(SENDMAN_STORAGE, SENDMAN_AUTO_USER_ID, String.class);
	}

	public void setAutoUserId(String autoUserId) {
		this.putObject(SENDMAN_STORAGE, SENDMAN_AUTO_USER_ID, autoUserId, true);
	}

	public void removeAutoUserId() {
		this.remove(SENDMAN_STORAGE, SENDMAN_AUTO_USER_ID, true);
	}


	/** --- Storage Helpers --- */

	protected String getString(String storeKey, String key, String defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(storeKey, Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	protected int getInt(String storeKey, String key, int defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(storeKey, Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	protected long getLong(String storeKey, String key, long defaultValue) {
		SharedPreferences sp = context.getSharedPreferences(storeKey, Context.MODE_PRIVATE);
		return sp.getLong(key, defaultValue);
	}

	protected boolean getBoolean(String storeKey, String key) {
		SharedPreferences sp = context.getSharedPreferences(storeKey, Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}

	protected <T> T getObject(String storageKey, String key, Type type) {
		String cachedJsonString = getString(storageKey, key, null);
		if (cachedJsonString == null) {
			return null;
		}

		Gson gson = new Gson();
		try {
			return gson.fromJson(cachedJsonString, type);
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "Cannot get object from local storage: " + storageKey + " for key: " + key + " with type: " + type, e);
		}

		return null;
	}

	protected boolean putString(String storageKey, String key, String value, boolean waitForCommit) {
		boolean success = true;
		SharedPreferences sp = context.getSharedPreferences(storageKey, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		if (waitForCommit) {
			success = editor.commit();
		} else {
			editor.apply();
		}
		return success;
	}

	protected boolean putInt(String storageKey, String key, int value, boolean waitForCommit) {
		boolean success = true;
		SharedPreferences sp = context.getSharedPreferences(storageKey, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		if (waitForCommit) {
			success = editor.commit();
		} else {
			editor.apply();
		}
		return success;
	}

	protected boolean putLong(String storageKey, String key, long value, boolean waitForCommit) {
		boolean success = true;
		SharedPreferences sp = context.getSharedPreferences(storageKey, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(key, value);
		if (waitForCommit) {
			success = editor.commit();
		} else {
			editor.apply();
		}
		return success;
	}

	protected boolean putBoolean(String storageKey, String key, boolean value, boolean waitForCommit) {
		boolean success = true;
		SharedPreferences sp = context.getSharedPreferences(storageKey, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		if (waitForCommit) {
			success = editor.commit();
		} else {
			editor.apply();
		}
		return success;
	}

	protected boolean putObject(String storageKey, String key, Object value, boolean waitForCommit) {
		Gson gson = new Gson();

		try {
			String objectJsonString = gson.toJson(value);
			return putString(storageKey, key, objectJsonString, waitForCommit);
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "Cannot get object from local storage: " + storageKey + " for key: " + key + " with value: " + value, e);
		}

		return false;
	}

	protected boolean remove(String storageKey, String key, boolean waitForCommit) {
		boolean success = true;
		SharedPreferences sp = context.getSharedPreferences(storageKey, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		if (waitForCommit) {
			success = editor.commit();
		} else {
			editor.apply();
		}
		return success;
	}

	protected void removeAll(String storageKey) {
		SharedPreferences sp = context.getSharedPreferences(storageKey, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.apply();
	}

	protected Map<String, ?> getAll(String storageKey) {
		SharedPreferences sp = context.getSharedPreferences(storageKey, Context.MODE_PRIVATE);
		return sp.getAll();
	}
}
