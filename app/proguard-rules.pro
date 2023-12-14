# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class com.bonface.openweather.OpenWeather
# Exception
-keep public class * extends java.lang.Exception
-keep class com.bonface.openweather.data.model.** { *; }

-keepattributes *Annotation*, Signature, Exception

-verbose
-printseeds seeds.txt
-printusage unused.txt
-printmapping mapping.txt
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-allowaccessmodification
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable
-repackageclasses ''

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keep,allowoptimization class com.google.android.libraries.maps.** { *; }

# Preserve all native method names and the names of their classes.
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
  public static <fields>;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn com.google.ads.**

-dontwarn com.oracle.svm.core.annotate.AutomaticFeature
-dontwarn com.oracle.svm.core.annotate.Delete
-dontwarn com.oracle.svm.core.annotate.Substitute
-dontwarn com.oracle.svm.core.annotate.TargetClass
-dontwarn com.oracle.svm.core.configure.ResourcesRegistry
-dontwarn io.grpc.internal.DnsNameResolverProvider
-dontwarn io.grpc.internal.PickFirstLoadBalancerProvider
-dontwarn org.graalvm.nativeimage.ImageSingletons
-dontwarn org.graalvm.nativeimage.hosted.Feature$BeforeAnalysisAccess
-dontwarn org.graalvm.nativeimage.hosted.Feature

-dontoptimize

-renamesourcefileattribute SourceFile

# Support Library
-keep class android.support.annotation.** { *; }
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class android.support.design.** { *; }
-keep class android.support.v7.widget.LinearLayoutCompat.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }
-keep public class com.google.android.gms.R* { *; }
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.support.v4.app.Fragment
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

-keep public class * extends View {
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
public void set*(...);
}

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn sun.misc.Unsafe
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }
-keep class org.apache.http.** { *; }
-keep class org.apache.commons.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class javax.inject.** { *; }
-dontwarn org.apache.http.**
-dontwarn org.apache.commons.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn retrofit2.**
-dontwarn sun.misc.**
-dontwarn com.opencsv.**
-dontwarn org.apache.commons.beanutils.**
-dontwarn org.apache.commons.collections.**

# Okhttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okhttp.**
# Okio
-dontwarn okio.**

# Okhttp
-dontwarn com.squareup.okhttp.**

# Google Play services
-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *

-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}

# Also keep - Enumerations. Keep the special static
# methods that are required in enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class android.support.v4.view.ViewPager
-keepclassmembers class android.support.v4.view.ViewPager$LayoutParams { *; }
-keep class android.support.v4.app.Fragment { *; }

-dontwarn java.lang.**
-keep class java.lang.**

-keep class android.location.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }
-keepnames class com.google.android.maps.** {*;}
-keep public class com.google.android.maps.** {*;}

-dontwarn com.google.android.maps.GeoPoint
-dontwarn com.google.android.maps.MapActivity
-dontwarn com.google.android.maps.MapView
-dontwarn com.google.android.maps.MapController
-dontwarn com.google.android.maps.Overlay

-keep class org.bouncycastle.** { *; }
-keepnames class org.bouncycastle.** { *; }
-keepattributes Signature
-dontwarn org.bouncycastle.**
-keep class org.spongycastle.** { *; }
-dontwarn org.spongycastle.**
-keep class org.xmlpull.** { *;}
-dontwarn org.xmlpull.**

# The Maps API uses custom Parcelables.
# Use this rule (which is slightly broader than the standard recommended one)
# to avoid obfuscating them.
-keepclassmembers class * implements android.os.Parcelable {
    static *** CREATOR;
}

# The Maps API uses serialization.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}

#Dagger
-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection
-dontwarn com.google.errorprone.annotations.*