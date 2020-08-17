package io.sendman.sendman;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SendManDatabase {

	private static final String SENDMAN_STORAGE = "sendman-storage";

	/** --- Data Members --- */

	protected Context context;

	/** --- Constructor --- */

	public SendManDatabase(Context context) {
		this.context = context;
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

	protected void remove(String storageKey, String key) {
		SharedPreferences sp = context.getSharedPreferences(storageKey, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.apply();
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
