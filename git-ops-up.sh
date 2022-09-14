#!/bin/bash

set -e

SOURCE=$1
COUNTRY=$2

tmpdir=$(mktemp -d)
trap "rm -rf $tmpdir" EXIT

git clone git@github.com:devgateway/amp-gitops.git $tmpdir
cd $tmpdir
./up.sh $@
git commit -m "up $SOURCE $COUNTRY"
git push
