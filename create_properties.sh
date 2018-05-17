#!/bin/sh

echo $LOCAL_PROPERTIES | base64 --decode  > local.properties
