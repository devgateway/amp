#!/bin/bash

set -e

SOURCE=$1
COUNTRY=$2

tmpdir=$(mktemp -d)
trap "rm -rf $tmpdir" EXIT

git clone git@github.com:devgateway/amp-gitops.git $tmpdir
cd $tmpdir
./down.sh $@
git commit -m "down $SOURCE $COUNTRY"
git push
