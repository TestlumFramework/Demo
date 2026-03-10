# update-schema.sh

A utility script to synchronize schema files from the main [Testlum](https://github.com/TestlumFramework/Testlum) repository.

## Purpose

This script fetches the latest schema definitions from the Testlum core repository and updates the local `schema/` folder with the professional schema files.

## Usage

```bash
./update-schema.sh
```

Run this script from the root of the `testlum-test-resources` repository.

## Prerequisites

- Git must be installed and available in PATH
- Internet connection to access GitHub
- Write permissions in the current directory

## What it does

1. Clones the Testlum repository (shallow clone, `main` branch only)
2. Clears the local `schema/` folder
3. Copies schema files from `engine/src/test/resources/subs/schema-professional/`
4. Removes the temporary clone directory

## Source

Schema files are pulled from:
```
https://github.com/TestlumFramework/Testlum
Branch: main
Path: engine/src/test/resources/subs/schema-professional/
```
