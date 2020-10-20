# SendMan

[![GitHub license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://raw.githubusercontent.com/sendmanio/sendman-android-sdk/master/LICENSE.md)

---

SendMan is a push notification management service for mobile apps. This SDK allows integrating your native Android apps with SendMan.

## Installation

SendMan is available through the [jCenter](https://bintray.com/bintray/jcenter) Gradle repository.
You should ensure google services are available, to allow FCM integration, at the root level of your project.

``` Groovy
dependencies {
    classpath 'com.android.tools.build:gradle:4.0.1'
    classpath 'com.google.gms:google-services:4.3.3'
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}
```

You also need to add these lines to your app module's gradle to add SendMan and apply Google's FCM plugin.

``` Groovy hl_lines="1 8"
apply plugin: 'com.google.gms.google-services'

dependencies {
    // ...
    implementation 'io.sendman.sdk:sendman:1.0.0'
}
```

## Integration

### Prerequisites

SendMan requires your app to be built with SDK 19 or above.

### Installation

1. **Initializing the SDK**

    ``` Java
    // Step 1: App-level identification: Initialize our SDK.
    SendMan.setAppConfig(new SendManConfig(appKey, appSecret));

    // Step 2: User-level identification: Identify your users with the unique ID your application uses to identify users.
    SendMan.setUserId("some-unique-id");
    ```

    If your users do not have unique IDs, use this syntax for us to automatically handle those users for you:

    ``` Java
    SendMan.setAppConfig(new SendManConfig(appKey, appSecret, true));
    ```

2. **Registering with the Android lifecycle**

    ``` Java
    public class YourApplication extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            SendMan.onCreate(this);
        }
    }
    ```

For a detailed integration guide (including integrating using Kotlin, storing custom user properties and allowing your users to manage their notifications), head to [our docs](https://docs.sendman.io/mobile-integration/android).

## Author

SendMan, hello@sendman.io

## License

SendMan is available under the MIT license. See the LICENSE file for more info.
