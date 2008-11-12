#!/usr/local/bin/bash
rake gems:install
rake db:migrate
mkdir -p tmp && touch tmp/restart.txt