#!/bin/bash
set -e

WORK_DIR=$(pwd)
URL='https://github.com/TestlumFramework/Testlum.git'

echo "Sync started with $URL"
git clone $URL --branch main --depth 1 --single-branch gitcopy
echo "Repo copied into 'gitcopy' folder"

mkdir -p "$WORK_DIR/schema"

echo "Cleaning schema folder"
rm -rf "$WORK_DIR/schema"/*

echo "Copying files from gitcopy to schema"
SCHEMA_DIR='engine/src/main/resources/schema'

cp -a "$WORK_DIR/gitcopy/$SCHEMA_DIR/." "$WORK_DIR/schema/"

echo "Deleting buffer dir gitcopy"
rm -rf gitcopy
echo "Done!"