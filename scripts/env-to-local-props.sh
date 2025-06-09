#!/bin/bash

FILE="local.properties"

if [ -f "$FILE" ]; then
    echo "$FILE already exists"
    exit 0
fi

check_env_var() {
    local var_value=$1
    local var_name=$2
    if [ -z "$var_value" ]; then
        echo "Error: $var_name environment variable is not set."
        exit 1
    fi
}

check_env_var "$GITHUB_USERNAME" "GITHUB_USERNAME"
check_env_var "$GITHUB_PASSWORD" "GITHUB_PASSWORD"

echo "Creating $FILE..."

{
    echo "githubPackagesUsername=$GITHUB_USERNAME"
    echo "githubPackagesPassword=$GITHUB_PASSWORD"
} >>"$FILE"

echo "$FILE created ✅"
