#!/bin/bash

# Set source and target directories
SOURCE_DIR="/Users/brianbrix/IdeaProjects/amp copy/amp/WEB-INF/src/org"
TARGET_DIR="/Users/brianbrix/IdeaProjects/amp/amp/src/main/java/org"

# Ensure source directory exists
if [ ! -d "$SOURCE_DIR" ]; then
    echo "Error: Source directory does not exist!"
    exit 1
fi

# Ensure target directory exists
if [ ! -d "$TARGET_DIR" ]; then
    echo "Creating target directory: $TARGET_DIR"
    mkdir -p "$TARGET_DIR"
fi

# Find all .java files in source directory
find "$SOURCE_DIR" -type f -name "*.java" | while read -r src_file; do
    # Get the relative path from source directory
    rel_path="${src_file#$SOURCE_DIR/}"

    # Construct the destination file path
    dest_file="$TARGET_DIR/$rel_path"

    # Check if the file already exists in the target directory
    if [ ! -f "$dest_file" ]; then
        # Ensure the target subdirectory exists
        mkdir -p "$(dirname "$dest_file")"

        # Copy the missing file
        cp "$src_file" "$dest_file"
        echo "Copied: $rel_path"
    else
        echo "Skipped (exists): $rel_path"
    fi
done

echo "Sync complete!"
