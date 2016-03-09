#!/bin/sh
browserify src/main.js -t brfs -s ampFilter -o dist/amp-filter.js
uglifyjs dist/amp-filter.js > dist/amp-filter.min.js
