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
