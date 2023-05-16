#!/bin/bash

set -e

AAPT="E:/android_sdk/build-tools/34.0.0-rc2/aapt"
DX="E:/android_sdk/build-tools/34.0.0-rc2/d8.bat"
ZIPALIGN="E:/android_sdk/build-tools/34.0.0-rc2/zipalign"
# APKSIGNER="/path/to/android-sdk/build-tools/26.0.1/apksigner" # /!\ version 26
PLATFORM="E:/android_sdk/platforms/android-31/android.jar"
SRC="E:/HealthApp/src"
LIB="E:/healthapp/libs"
ADB="E:/android_sdk/platform-tools/adb"

echo "Cleaning..."
rm -rf obj/*
rm -rf src/com/healthapp/homepage/R.java
# rm -rf src/com/healthapp/mainpage/R.java

echo "Generating R.java file..."
$AAPT package -f -m -J src -M AndroidManifest.xml -S res -I $PLATFORM

echo "Compiling..."
# javac -d obj --class-path "$PLATFORM;$SRC" src/com/healthapp/homepage/*.java src/com/healthapp/mainpage/*.java
javac -d obj --class-path "$PLATFORM;$SRC;$LIB/*" src/com/healthapp/homepage/*.java src/com/healthapp/mainpage/*.java
# javac -d obj -classpath src -bootclasspath $PLATFORM -source 1.7 -target 1.7 src/com/healthapp/homepage/R.java
# https://mvnrepository.com/artifact/javax.websocket/javax.websocket-api

echo "Translating in Dalvik bytecode..."
$DX --output=bin obj/com/healthapp/mainpage/*.class obj/com/healthapp/homepage/*.class libs/*.jar
# $DX --output=bin obj/com/healthapp/homepage/*.class

echo "Making APK..."
$AAPT package -f -m -F bin/healthapp.unaligned.apk -M AndroidManifest.xml -S res -I $PLATFORM
cp bin/classes.dex .
$AAPT add bin/healthapp.unaligned.apk classes.dex
# $AAPT add bin/healthapp.unaligned.apk libs/*.jar

echo "Aligning and signing APK..."
# $APKSIGNER sign --ks mykey.keystore bin/hello.unaligned.apk
$ZIPALIGN -f 4 bin/healthapp.unaligned.apk bin/healthapp.apk
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore mykey.keystore -storepass password bin/healthapp.apk SID
# expect "Enter Passphrase for keystore:"
# send "KEY@sid25"

if [ "$1" == "test" ]; then
	echo "Launching..."
	$ADB install -r bin/healthapp.apk
	$ADB shell am start -n com.healthapp.homepage/.HomePageActivity
fi

if [ "$2" == "log" ]; then
    echo "Logging..."
    # $ADB logcat -c
	# $ADB logcat -s HomePageActivity -s MainActivity -s *:E
    $ADB logcat -s HealthApp *:D > log.txt
fi