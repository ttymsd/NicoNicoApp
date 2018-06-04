#!/bin/sh

mkdir -p $ANDROID_HOME/licenses
echo $ANDROID_SDK_LICENSE > /usr/local/android-sdk-linux/licenses/android-sdk-license
echo $LOCAL_PROPERTIES | base64 --decode  > local.properties
