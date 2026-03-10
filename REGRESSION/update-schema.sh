#!/bin/bash

WORK_DIR=$(pwd)

URL='https://github.com/TestlumFramework/Testlum.git'
echo "Sync started with $URL"
git clone $URL --branch main --depth 1 --single-branch gitcopy
echo "Repo copied into 'gitcopy' folder"

echo "Cleaning schema folder"
rm -rf "$WORK_DIR"/schema/*

echo "Copying files from gitcopy to schema"
SCHEMA_DIR='engine/src/test/resources/subs/schema-professional'
cp -r "$WORK_DIR"/gitcopy/"$SCHEMA_DIR"/* "$WORK_DIR"/schema/

echo "Deleting buffer dir gitcopy"
rm -rf gitcopy