package io.sendman.sendman;

import android.content.Context;

public class SendManContextHelper {

	public static int getDrawableId(String name) {
		Context context = SendMan.getApplicationContext();
		return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
	}
}
