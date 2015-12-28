#!/usr/bin/env node

var fs = require('fs');
var browserify = require('browserify');
var uglify = require('uglify-stream');

var bundleStream = browserify() // CONSTANTIN - {debug: true} for development
  .add('./index.js')
  .require([
    'backbone',
    // bootstrap registers as jQ plugin, does not need to be here.
    'd3',
    'jquery',
    'underscore'
  ])
  .bundle();

bundleStream
  .pipe(fs.createWriteStream('common.js'));

bundleStream
  .pipe(uglify())  // CONSTANTIN - comment this one for development
  .pipe(fs.createWriteStream('common.min.js'));
