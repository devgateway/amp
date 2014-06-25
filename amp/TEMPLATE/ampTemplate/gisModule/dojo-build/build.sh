#!/usr/bin/env bash

# Base directory for this entire project
BASEDIR=$(cd $(dirname $0) && pwd)

# Source directory for unbuilt code
SRCDIR="$BASEDIR/../dev/app"

# Directory containing dojo build utilities
TOOLSDIR="$SRCDIR/js/libs/util"

# Destination directory for built code
DISTDIR="$BASEDIR/dojo-dist"

# Module ID of the main application package loader configuration
LOADERMID="app/run"

# Main application package loader configuration
LOADERCONF="$SRCDIR/$LOADERMID.js"

# Main application package build configuration
PROFILE="$BASEDIR/profiles/build.profile.js"

cd "$TOOLSDIR"

if which node >/dev/null; then
  node ../../dojo/dojo.js load=build --require "$LOADERCONF" --profile "$PROFILE" --releaseDir "$DISTDIR" $@
elif which java >/dev/null; then
  java -Xms256m -Xmx256m  -cp ../shrinksafe/js.jar:../closureCompiler/compiler.jar:../shrinksafe/shrinksafe.jar org.mozilla.javascript.tools.shell.Main  ../../dojo/dojo.js baseUrl=../../dojo load=build --require "$LOADERCONF" --profile "$PROFILE" --releaseDir "$DISTDIR" $@
else
  echo "Need node.js or Java to build!"
  exit 1
fi
