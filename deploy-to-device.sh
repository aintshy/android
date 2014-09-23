#!/bin/bash
set -x
set -e

mvn clean install
adb uninstall com.aintshy.android
adb install -r target/android-1.0-SNAPSHOT.apk

