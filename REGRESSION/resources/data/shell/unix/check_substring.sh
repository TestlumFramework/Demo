#!/bin/sh

echo "Checking if '$1' contains '$2'..."
echo "$1" | grep -q "$2"
RESULT=$?
exit $RESULT