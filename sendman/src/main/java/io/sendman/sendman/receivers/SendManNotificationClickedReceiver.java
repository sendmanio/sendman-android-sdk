package io.sendman.sendman.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import io.sendman.sendman.SendManLifecycleHandler;
import io.sendman.sendman.models.SendManNotificationMetadata;

public class SendManNotificationClickedReceiver extends BroadcastReceiver {

	private static final String TAG = SendManNotificationClickedReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent openAppIntent = context
				.getPackageManager()
				.getLaunchIntentForPackage(context.getPackageName())
				.setPackage(null)
				.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(openAppIntent);

		SendManNotificationMetadata metadata = SendManNotificationMetadata.fromIntent(intent, context.getPackageName());
		SendManLifecycleHandler.getInstance().onNotificationClicked(metadata);
	}
}
