
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.view.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
 -keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
 native <methods>;
}

-keep public class * extends android.view.View {
 public <init>(android.content.Context);
 public <init>(android.content.Context, android.util.AttributeSet);
 public <init>(android.content.Context, android.util.AttributeSet, int);
 public void set*(...);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
 public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
 public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
 public static **[] values();
 public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
 public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
 public static <fields>;
}

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-keep class android.support.v7.internal.** { *; }
-keep interface android.support.v7.internal.** { *; }
-keep class android.util.Xml..** { *; }
-keep class android.support.v7.widget.ShareActionProvider { *; }
-keep class android.support.v7.widget.** { *; }
-keep interface android.support.v7.widget.** { *; }

-keep class com.individual.noteapp.models.** { *; }

-keepattributes Annotation,EnclosingMethod,Signature
-keep public class your.class.* {
public void set(*);
public ** get*();
}

-dontwarn com.google.common.**

-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}


-keep class .R
-keep class **.R$* {
    <fields>;
}

-dontwarn com.polites.android.**

-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }


