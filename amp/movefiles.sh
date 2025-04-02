#!/bin/bash

# Source and destination directories
SOURCE_DIR="/Users/brianbrix/IdeaProjects/amp/amp/src/main/java/org"
DEST_DIR="/Users/brianbrix/IdeaProjects/amp/amp/src/main/resources/org"

# Check if both arguments are provided
if [ -z "$SOURCE_DIR" ] || [ -z "$DEST_DIR" ]; then
    echo "Usage: $0 <source_directory> <destination_directory>"
    exit 1
fi

# Ensure the source directory exists
if [ ! -d "$SOURCE_DIR" ]; then
    echo "Error: Source directory does not exist."
    exit 1
fi

# Ensure the destination directory exists, create if not
mkdir -p "$DEST_DIR"

# Find and move .html files while preserving directory structure
find "$SOURCE_DIR" -type f -name "*.hbm.xml" | while read -r file; do
    # Get the directory structure relative to source directory
    RELATIVE_PATH="${file#$SOURCE_DIR/}"
    RELATIVE_DIR="$(dirname "$RELATIVE_PATH")"

    # Create the directory in the destination if it doesn't exist
    mkdir -p "$DEST_DIR/$RELATIVE_DIR"

    # Move the file
    mv "$file" "$DEST_DIR/$RELATIVE_DIR/"
done

echo "All .html files moved successfully from $SOURCE_DIR to $DEST_DIR while maintaining directory structure."
