#!/usr/local/bin/bash
rake gems:install
rake db:migrate
gem list > installed.txt
mkdir -p tmp && touch tmp/restart.txt