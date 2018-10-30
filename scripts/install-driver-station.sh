#!/usr/bin/env bash
set -e

##
# Install the Driver Station app on the attached Android Device.
#
# WARNING: DO NOT INSTALL THE DRIVER STATION ON THE ROBOT CONTROLLER!!!
##

cd "$(dirname "$0")/.."

if ! (./scripts/_get_device_name.sh | grep "\-DS"); then
	echo "ERROR: Refusing to install on non-driver station device: $(./scripts/_get_device_name.sh)!"
	exit 1
fi

adb install -r doc/apk/FtcDriverStation-release.apk
