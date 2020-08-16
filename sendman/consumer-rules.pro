# For GSON to generall work
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keep class com.google.gson.** { *; }

# Data models going through serialization (and deserialization) using GSON
-keepclassmembers enum * { *; }
-keep class io.sendman.sendman.models.** { *; }

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# For androix.lifecycle to function
-keepclassmembers class * {
  @androidx.lifecycle.OnLifecycleEvent *;
}

-keepclassmembers class * implements androidx.lifecycle.LifecycleObserver {
    <init>(...);
}
-keep class * implements androidx.lifecycle.LifecycleObserver {
    <init>(...);
}

-keepclassmembers class androidx.lifecycle.** { *; }
-keep class androidx.lifecycle.** { *; }
-dontwarn androidx.lifecycle.**