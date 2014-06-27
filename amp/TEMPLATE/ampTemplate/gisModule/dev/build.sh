#!/usr/bin/env bash

# Base directory for this entire project
BASEDIR=$(cd $(dirname $0) && pwd)

# Source directory for unbuilt code
SRCDIR="$BASEDIR/app"

# Directory containing dojo build utilities
TOOLSDIR="$SRCDIR/js/libs/vendor/util"

# Destination directory for built code
DISTDIR="$BASEDIR/../dojo-dist"

# Module ID of the main application package loader configuration
LOADERMID="js/run"

# Main application package loader configuration
LOADERCONF="$SRCDIR/$LOADERMID.js"

# Main application package build configuration
PROFILE="$BASEDIR/build.profile.js"

echo "$TOOLSDIR"
cd "$TOOLSDIR"
node "$SRCDIR/js/libs/vendor/dojo/dojo.js" load=build --require "$LOADERCONF" --profile "$PROFILE" --releaseDir "$DISTDIR" $@

