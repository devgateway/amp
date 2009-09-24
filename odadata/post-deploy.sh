#!/usr/local/bin/bash
gem install acts_as_reportable
rake gems:install
rake db:migrate
mkdir -p tmp && touch tmp/restart.txt