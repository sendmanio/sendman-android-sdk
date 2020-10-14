package io.sendman.sendman.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import io.sendman.sendman.SendMan;
import io.sendman.sendman.SendManContextHelper;
import io.sendman.sendman.SendManLifecycleHandler;
import io.sendman.sendman.models.SendManNotificationMetadata;
import io.sendman.sendman.receivers.SendManNotificationClickedReceiver;
import io.sendman.sendman.receivers.SendManNotificationDismissedReceiver;

public class SendManFirebaseMessagingService extends FirebaseMessagingService {

	private static final String TAG = SendManFirebaseMessagingService.class.getSimpleName();
	private Random random = new SecureRandom();
	private Map<String, Integer> activityIdsToNotificationIds = new HashMap<>();

	@Override
	public void onNewToken(@NonNull String token) {
		super.onNewToken(token);
		SendMan.setFCMToken(token);
	}

	@Override
	public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);

		SendManNotificationMetadata metadata = SendManNotificationMetadata.fromData(remoteMessage.getData());
		if (metadata == null) {
			Log.i(TAG, "Received a remote message without SendMan data - will not process it in SendMan.");
			return;
		}

		SendManLifecycleHandler.getInstance().onNotificationReceived(metadata);

		if (shouldDisplayForegroundNotification() || !SendManLifecycleHandler.getInstance().isInForeground()) {
			displayNotification(metadata);
		}
	}

	/* --- Configuration --- */

	protected boolean shouldDisplayForegroundNotification() {
		return true;
	}

	protected void displayNotification(SendManNotificationMetadata metadata) {
		String title = metadata.getTitle();
		String notificationBody = metadata.getBody();

		createNotificationChannel(metadata);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, metadata.getCategoryId())
				.setSmallIcon(getSmallIconId(metadata))
				.setContentTitle(title)
				.setContentText(notificationBody)
				.setPriority(NotificationCompat.PRIORITY_MAX) // TODO: priority
				.setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_VIBRATE)
				.setContentIntent(createIntentForReceiver(SendManNotificationClickedReceiver.class, metadata))
				.setDeleteIntent(createIntentForReceiver(SendManNotificationDismissedReceiver.class, metadata))
				.setAutoCancel(true);

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

		// notificationId is a unique int for each notification that you must define
		notificationManager.notify(metadata.getActivityId(), getNotificationIdForActivityId(metadata.getActivityId()), mBuilder.build());
	}

	private void createNotificationChannel(SendManNotificationMetadata metadata) {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			// TODO: revisit priority
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = new NotificationChannel(metadata.getCategoryId(), metadata.getCategoryName(), importance);
			channel.setDescription(metadata.getCategoryDescription());
			// Register the channel with the system; you can't change the importance or other notification behaviors after this
			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			notificationManager.createNotificationChannel(channel);
		}
	}

	private int getNotificationIdForActivityId(String activityId) {
		if (!activityIdsToNotificationIds.containsKey(activityId)) {
			activityIdsToNotificationIds.put(activityId, Math.abs(random.nextInt()));
		}

		return activityIdsToNotificationIds.get(activityId);
	}

	private PendingIntent createIntentForReceiver(Class<? extends BroadcastReceiver> receiver, SendManNotificationMetadata metadata) {
		Intent intent = new Intent(this, receiver);
		intent.putExtra(SendManNotificationMetadata.BUNDLE_EXTRA_IDENTIFIER, new Gson().toJson(metadata));
		return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private static int getSmallIconId(SendManNotificationMetadata metadata) {
		String smallIconFromNotification = metadata.getSmallIconFilename();
		if (smallIconFromNotification != null) {
			int smallIconId = SendManContextHelper.getDrawableId(smallIconFromNotification);
			if (smallIconId != 0) return smallIconId;
		}

		String smallIconFilenameFromConfig = SendMan.getConfig().getSmallIconFilename();
		if (smallIconFilenameFromConfig != null) {
			int smallIconId = SendManContextHelper.getDrawableId(smallIconFilenameFromConfig);
			if (smallIconId != 0) return smallIconId;
		}

		int smallIconId = SendManContextHelper.getDrawableId("ic_default_small_sendman_notification");
		if (smallIconId != 0) return smallIconId;

		return android.R.drawable.ic_popup_reminder;
	}

}
