#!/usr/local/bin/bash
rake db:migrate
mkdir -p tmp && touch tmp/restart.txt