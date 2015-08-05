var glob      = require('plumber-glob');
var uglify    = require('plumber-uglifyjs')();
var concat    = require('plumber-concat');
var write     = require('plumber-write');

module.exports = function(pipelines) {

    // Pass all JS files through pipeline
    pipelines['concat'] = [
      glob('test/fixtures/source.js',
           'test/fixtures/library.js'),
      concat('concatenated'),
      write('test/fixtures')
    ];

    // Minify all files and write to out directory
    pipelines['uglify'] = [
      glob('test/fixtures/concatenated.js'),
      uglify,
      write('test/fixtures')
    ];

};
