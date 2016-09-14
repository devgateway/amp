#!/usr/bin/env bash

# By default allow groups to edit new files. Helps with limiting the web server
# user to only create and edit files inside a single folder, and not change the
# application files.
umask 0002
