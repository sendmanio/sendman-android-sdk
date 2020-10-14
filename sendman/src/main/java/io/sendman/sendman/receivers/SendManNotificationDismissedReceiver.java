package io.sendman.sendman.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import io.sendman.sendman.SendManLifecycleHandler;
import io.sendman.sendman.models.SendManNotificationMetadata;

public class SendManNotificationDismissedReceiver extends BroadcastReceiver {

	private static final String TAG = SendManNotificationDismissedReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		String activityId = extras != null ? extras.getString("activityId", "no activity") : "no activity";
		Log.i(TAG, "Notification deleted: " + activityId);

		SendManNotificationMetadata metadata = SendManNotificationMetadata.fromIntent(intent, context.getPackageName());
		SendManLifecycleHandler.getInstance().onNotificationDismissed(metadata);
	}
}
