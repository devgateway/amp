var browserify = require('browserify');
var source = require('vinyl-source-stream');
var gulp = require('gulp');
var del = require('del');
var $ = require('gulp-load-plugins')();
var log = require('fancy-log');


var paths = {
  root: './src',
  ampRoot: '../../../../',
  rootStuff: './src/*.{html,txt,xml}',
  dist: './dist/',
  scripts: {
    entry: './index.js',
    compiled: './src/compiled-js/',
    sources: [
      './src/models/*.js',
      './src/views/*.js',
      './src/collections/*.js',
      './gulpfile.js'
    ]
}
};

function _browserifier(entry, destFolder, destName, options) {
  var bundler = browserify(entry, options);
  bundler.transform('brfs');
  var bundle = function() {
    log('bundle: start');
    return bundler.bundle()
      .on('end', function() { log('bundle: finished'); })
      .on('error', function(e) { log('bundle: error: ', e); })
      .pipe(source(destName))
      .pipe(gulp.dest(destFolder));
  };

  return {
    bundler: bundler,
    bundle: bundle
  };
}


gulp.task('lint', function() {
  return gulp.src(paths.scripts.sources)
    .pipe($.plumber())
    .pipe($.jscs())
    .pipe($.jshint())
    .pipe($.jshint.reporter('jshint-stylish'));
});


gulp.task('browserify', function() {
  var stuff = _browserifier(paths.scripts.entry,
    paths.scripts.compiled, 'amp-boilerplate.js',{debug: true, standalone: 'amp-boilerplate'});
  return stuff.bundle();
});


gulp.task('clean', function(done) {
  del(paths.dist, { force: true });
  done();
});


gulp.task('build-js', gulp.series('browserify', function() {
  return gulp.src(paths.scripts.compiled + 'amp-boilerplate.js')
    .pipe($.streamify($.uglify))
    .pipe(gulp.dest(paths.dist));
}));

gulp.task('build-rootstuff', function() {
  return gulp.src(paths.rootStuff)
    .pipe(gulp.dest(paths.dist));
});

gulp.task('build', gulp.series('clean', 'build-js', 'build-rootstuff'));
