package io.sendman.sendman;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import io.sendman.sendman.models.SendManNotificationMetadata;
import io.sendman.sendman.models.SendManSDKEvent;

public class SendManLifecycleHandler implements LifecycleObserver {

	private static final String TAG = SendManLifecycleHandler.class.getSimpleName();
	private static SendManLifecycleHandler instance = null;

	private boolean inForeground;
	private boolean isLaunch = true;
	private SendManNotificationMetadata latestUnprocessedNotificationMetadata;
	private Set<String> reportedActivityIds = new HashSet<>();

	public synchronized static SendManLifecycleHandler getInstance() {
		if (instance == null) {
			instance = new SendManLifecycleHandler();
		}
		return instance;
	}

	public void onCreate() {
		FirebaseInstanceId.getInstance().getInstanceId()
				.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
					@Override
					public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

						// Get new Instance ID token
						InstanceIdResult result = task.getResult();
						if (result != null) {
							String token = result.getToken();
							Log.i(TAG, "Received FCM token: " + token);
							SendMan.setFCMToken(token);
						}
					}
				});

		ProcessLifecycleOwner.get().getLifecycle().addObserver(SendManLifecycleHandler.getInstance());
	}

	/* --- Public Methods --- */

	public boolean isInForeground() {
		return inForeground;
	}

	public String getNotificationRegistrationState() {
		return getChannelConfiguration() == null ? "Off" : "On";
	}


	/* --- Broadcast Receiver callbacks --- */

	public void onNotificationReceived(SendManNotificationMetadata metadata) {
		if (metadata == null) {
			Log.w(TAG, "Cannot report SendMan data for null data.");
			return;
		}

		if (isInForeground()) {
			SendManSDKEvent event = new SendManSDKEvent("Foreground Notification Received", null);
			event.setActivityId(metadata.getActivityId());
			event.setTemplateId(metadata.getTemplateId());
			event.setAppState("Active");
			SendManDataCollector.addSdkEvent(event);
		}
	}

	public void onNotificationClicked(SendManNotificationMetadata metadata) {
		latestUnprocessedNotificationMetadata = metadata;
	}

	// TODO
	public void onNotificationDismissed(SendManNotificationMetadata metadata) {}

	/* --- LifeCycle events --- */

	@OnLifecycleEvent(Lifecycle.Event.ON_START)
	public void onMoveToForeground() {
		inForeground = true;

		if (shouldReportEngagement()) {
			SendManSDKEvent engagementEvent = new SendManSDKEvent("Background Notification Opened", null);
			String activityId = latestUnprocessedNotificationMetadata.getActivityId();
			engagementEvent.setActivityId(activityId);
			engagementEvent.setTemplateId(latestUnprocessedNotificationMetadata.getTemplateId());
			engagementEvent.setAppState(isLaunch ? "Killed" : "Background");
			SendManDataCollector.addSdkEvent(engagementEvent);
			reportedActivityIds.add(activityId);
		}

		SendManSDKEvent foregroundEvent;
		if (isLaunch) {
			foregroundEvent = new SendManSDKEvent("App launched", null);
			foregroundEvent.setAppState("Killed");
		} else {
			foregroundEvent = new SendManSDKEvent("App entered foreground", null);
			foregroundEvent.setAppState("Background");
		}
		SendManDataCollector.addSdkEvent(foregroundEvent);

		latestUnprocessedNotificationMetadata = null;
		isLaunch = false;
	}

	@OnLifecycleEvent(Lifecycle.Event.ON_STOP)
	public void onMoveToBackground() {
		inForeground = false;
	}

	/* --- Private Methods --- */

	private Map<String, Boolean> getChannelConfiguration() {
		Context applicationContext = SendMan.getApplicationContext();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager manager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
			if (!manager.areNotificationsEnabled()) {
				return null;
			}

			List<NotificationChannel> channels = manager.getNotificationChannels();
			Map<String, Boolean> channelsToEnabled = new HashMap<>();
			for (NotificationChannel channel : channels) {
				channelsToEnabled.put(channel.getId(), channel.getImportance() != NotificationManager.IMPORTANCE_NONE);
			}
			return channelsToEnabled;
		} else {
			return NotificationManagerCompat.from(applicationContext).areNotificationsEnabled() ? Collections.<String, Boolean>emptyMap() : null;
		}
	}

	private boolean shouldReportEngagement() {
		return (
			latestUnprocessedNotificationMetadata != null &&
			!reportedActivityIds.contains(latestUnprocessedNotificationMetadata.getActivityId()) &&
			isRecentTimestamp(latestUnprocessedNotificationMetadata.getDeserializationTimestamp())
		);
	}

	private boolean isRecentTimestamp(long timestamp) {
		return System.currentTimeMillis() - timestamp < 1000;
	}
}
