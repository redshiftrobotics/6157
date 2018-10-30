#!/usr/bin/env bash

cd "$(dirname "$0")/.."

# Just trust me when I say that this will print "2856-RC" or whatever -@ariporad

adb shell dumpsys wifi |
	sed -n 's/.*SET device_name \([a-zA-Z0-9]\{1,\}-[DR][SC]\) -> true/\1/p' |
	head -n 1 |
	tr -d '\rn'
