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
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# 对于R（资源）类中的静态方法不能被混淆
-keepclassmembers class **.R$* {
 public static <fields>;
}
#自定义部分
#一颗星表示只是保持该包下的类名，而子包下的类名还是会被混淆；两颗星表示把本包和所含子包下的类名都保持；用以上方法保持类后，你会发现类名虽然未混淆，但里面的具体方法和变量命名还是变了，这时如果既想保持类名，又想保持里面的内容不被混淆，我们就需要以下方法了.
-keep class com.de.rocket.Rocket{ * ;}
-keep class com.de.rocket.app.** { *; }
-keep class com.de.rocket.bean.** { *; }
-keep class com.de.rocket.cons.** { *; }
#-keep class com.de.rocket.helper.** { *; } 除了helper文件夹,其他都不混淆
-keep class com.de.rocket.listener.** { *; }
-keep class com.de.rocket.ue.** { *; }
-keep class com.de.rocket.utils.** { *; }