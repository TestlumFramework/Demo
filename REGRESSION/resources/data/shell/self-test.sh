#!/bin/bash

MODE=$1

case $MODE in
  "success")
    echo "Standard output message"
    echo "Line 2 of output"
    exit 0
    ;;

  "success")
    echo "This is a normal message"
    echo "CRITICAL: This is an error message"
    exit 1
    ;;

  "slow")
    echo "Starting slow process..."
    sleep 2
    echo "Process complete"
    exit 0
    ;;

  "env")
    echo "TEST_VAR_VALUE=${TEST_VAR:-NOT_SET}"
    exit 0
    ;;

  *)
    echo "Usage: $0 {success|error|slow|env|math}"
    exit 3
    ;;
esac