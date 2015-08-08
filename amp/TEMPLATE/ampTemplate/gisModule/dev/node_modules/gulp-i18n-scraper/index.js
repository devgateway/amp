//tut https://github.com/gulpjs/gulp/blob/master/docs/writing-a-plugin/guidelines.md

// through2 is a thin wrapper around node transform streams
var through = require('through2');
var gutil = require('gulp-util');
var PluginError = gutil.PluginError;
var cheerio = require('cheerio');
var _ = require('underscore');
var StringDecoder = require('string_decoder').StringDecoder;


// consts
const PLUGIN_NAME = 'gulp-i18n-scraper';

function prefixStream(selectorText) {
  var stream = through();
  stream.write(selectorText);
  return stream;
}

// plugin level function (dealing with files)
function gulpPrefixer(selectorText) {

  if (!selectorText) {
    throw new PluginError(PLUGIN_NAME, 'Missing prefix text!');
  }

  selectorText = new Buffer(selectorText); // allocate ahead of time

  // creating a stream through which each file will pass
  var stream = through.obj(function(file, enc, cb) {
    if (file.isNull()) {
       // do nothing if no contents
    }

    //it goes through here, cause we use concat can't do streams.
    if (file.isBuffer()) {
      //console.log('processing file ', file.path);

      $ = cheerio.load(file.contents.toString('utf8'));//'<p>file</p>');
      var str = '';
      $(selectorText).each(function(){
        var txt = $(this).text();
        var data = $(this).data('i18n');

        //only print if not dynamic.
        if(data.indexOf('<%=') < 0){
          str += '"' + data + '": "' + txt + '",\n';
        }
        //console.log('txt', txt, data);
      });
      file.contents = new Buffer(str);
    }

    if (file.isStream()) {
      file.contents = file.contents.pipe(prefixStream(selectorText));
    }

    this.push(file);

    return cb();
  });

  // returning the file stream
  return stream;
};

// exporting the plugin main function
module.exports = gulpPrefixer;
