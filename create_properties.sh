#!/bin/sh

echo $ANDROID_LICENSES | base64 --decode > /usr/local/android-sdk-linux/licenses
echo $LOCAL_PROPERTIES | base64 --decode  > local.properties
