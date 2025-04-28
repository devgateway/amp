#!/bin/bash

# Set your base directory (can be passed as an argument)
BASE_DIR="${1:-generated}"  # Default to "generated" if not specified

# Define the search pattern
PATTERN="Generated on: 2025.04.26 at"

# Temporary file to store matched file paths
MATCHED_FILES=$(mktemp)

# Step 1: Find matching files inside BASE_DIR
grep -rlF "$PATTERN" "$BASE_DIR" > "$MATCHED_FILES"

# Step 2: Remove matching files from Git tracking (but keep locally)
while read -r file; do
    if [ -f "$file" ]; then
        echo "Untracking $file"
        git rm --cached "$file"

        # Add to .gitignore if not already present
        if ! grep -Fxq "$file" .gitignore; then
            echo "$file" >> .gitignore
        fi
    fi
done < "$MATCHED_FILES"

# Step 3: Clean up
rm "$MATCHED_FILES"

# Step 4: Stage changes
git add .gitignore

# Step 5: Commit
git commit -m "Remove JAXB generated files from $BASE_DIR and add to .gitignore"

echo "✅ Done! Files inside $BASE_DIR processed."
