package io.sendman.sendman;

import java.util.ArrayList;
import java.util.HashMap;

import io.sendman.sendman.models.SendManCategory;

public class SendMan {

    private static SendMan instance = null;

    private SendManConfig config;
    private String smUserId;
    private ArrayList<SendManCategory> categories;


    public synchronized static SendMan getInstance() {
        if (instance == null) {
            instance = new SendMan();
        }
        return instance;
    }

    public static SendManConfig getConfig() {
        return SendMan.getInstance().config;
    }

    public static String getUserId() {
        return SendMan.getInstance().smUserId;
    }

    public static ArrayList<SendManCategory> getCategories() {
        return SendMan.getInstance().categories != null ? SendMan.getInstance().categories  : new ArrayList<SendManCategory>();
    }

    public static void setAppConfig(SendManConfig config) {
        SendMan.getInstance().config = config;
    }

    public static void setUserId(String smUserId) { //TODO
        SendMan.getInstance().smUserId = smUserId;
        SendManDataCollector.getInstance().startSession();
        SendManAPIHandler.getCategories(null);
    }

    public static void setUserCategories(ArrayList<SendManCategory> categories) {
        SendMan.getInstance().categories = categories;
    }

    public static void updateUserCategories(ArrayList<SendManCategory> categories) {

    }

    public static void setUserProperties(HashMap<String, String> properties) {
        SendManDataCollector.setUserProperties(properties);
    }

    public static void addUserEvent(String eventName) {
        SendMan.addGenericUserEvent(eventName, null);
    }

    public static void addUserEvent(String eventName, String value) {
        SendMan.addGenericUserEvent(eventName, value);
    }

    public static void addUserEvent(String eventName, int value) {
        SendMan.addGenericUserEvent(eventName, value);
    }

    public static void addUserEvent(String eventName, Boolean value) {
        SendMan.addGenericUserEvent(eventName, value);
    }

    private static void addGenericUserEvent(String eventName, Object value) {
        HashMap<String, Object> events = new HashMap<>();
        events.put(eventName, value);
        SendManDataCollector.addUserEvents(events);
    }
}


//NSString *const SMAPNTokenKey = @"SMAPNToken";
//
//        + (void)setAPNToken:(NSString *)token {
//        [SMDataCollector setUserProperties:@{SMAPNTokenKey: token}];
//        }
//
//        + (void)updateUserCategories:(NSArray *)categories {
//        Sendman *sendman = [Sendman instance];
//        sendman.categories = categories;
//        [SMCategoriesHandler updateCategories:categories];
//        }
//
//
//

//
//        + (void)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
//        SMLifecycleHandler *manager = [SMLifecycleHandler sharedManager];
//        [manager application:application didFinishLaunchingWithOptions:launchOptions];
//        }
//
//        + (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
//        [[SMLifecycleHandler sharedManager] application:application didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];
//        }
//
//        + (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
//        [[SMLifecycleHandler sharedManager] application:application didFailToRegisterForRemoteNotificationsWithError:error];
//        }
//
//        + (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult result))completionHandler {
//        [[SMLifecycleHandler sharedManager] application:application didReceiveRemoteNotification:userInfo fetchCompletionHandler:completionHandler];
//        }
//
//        + (void)userNotificationCenter:(UNUserNotificationCenter *)center openSettingsForNotification:(UNNotification *)notification {
//        [[SMLifecycleHandler sharedManager] userNotificationCenter:center openSettingsForNotification:notification];
//        }
//
//        + (void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler {
//        [[SMLifecycleHandler sharedManager] userNotificationCenter:center willPresentNotification:notification withCompletionHandler:completionHandler];
//        }
//
//        + (void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler {
//        [[SMLifecycleHandler sharedManager] userNotificationCenter:center didReceiveNotificationResponse:response withCompletionHandler:completionHandler];
//        }
//
//        + (void)registerForRemoteNotifications:(void (^)(BOOL granted))success {
//        [[SMLifecycleHandler sharedManager] registerForRemoteNotifications:success];
//        }