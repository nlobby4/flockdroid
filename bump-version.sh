#!/bin/bash
# Usage: ./bump-version.sh [patch|minor|major]
# Default: patch

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
VERSION_FILE="$SCRIPT_DIR/version.properties"

if [ ! -f "$VERSION_FILE" ]; then
    echo "Error: version.properties not found"
    exit 1
fi

current=$(grep VERSION_NAME "$VERSION_FILE" | cut -d= -f2)
current_code=$(grep VERSION_CODE "$VERSION_FILE" | cut -d= -f2)

IFS='.' read -r major minor patch <<< "$current"

bump_type=${1:-patch}

case $bump_type in
    patch) patch=$((patch + 1)) ;;
    minor) minor=$((minor + 1)); patch=0 ;;
    major) major=$((major + 1)); minor=0; patch=0 ;;
    *)
        echo "Usage: $0 [patch|minor|major]"
        exit 1
        ;;
esac

new_version="$major.$minor.$patch"
new_code=$((current_code + 1))

sed -i "s/VERSION_NAME=.*/VERSION_NAME=$new_version/" "$VERSION_FILE"
sed -i "s/VERSION_CODE=.*/VERSION_CODE=$new_code/" "$VERSION_FILE"

echo "Bumped version: $current -> $new_version"
echo "Bumped code: $current_code -> $new_code"
