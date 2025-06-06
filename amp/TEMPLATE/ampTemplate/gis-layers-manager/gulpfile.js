var browserify = require('browserify');
var source = require('vinyl-source-stream');
var gulp = require('gulp');
var rimraf = require('rimraf');
var $ = require('gulp-load-plugins')();
var log = require('fancy-log');
var beep = require('beeper');


var paths = {
  root: './src',
  ampRoot: '../../../../',
  rootStuff: './src/*.{html,txt,xml}',
  dist: './dist/',
  scripts: {
    entry: './src/index.js',
    compiled: './src/compiled-js/',
    sources: [
      './src/models/*.js',
      './src/views/*.js',
      './src/collections/*.js',
      './gulpfile.js'
    ]
},
 stylesheets: {
    entry: './src/less/gis-layers-manager.less',
    compiled: './src/compiled-css/',
    sources: ['./src/less/**/*.less'],
    testStyles: './bower_components/mocha/mocha.css'
  },
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
  gulp.src(paths.scripts.sources)
    .pipe($.plumber())
    .pipe($.jscs())
    .pipe($.jshint())
    .pipe($.jshint.reporter('jshint-stylish'));
});


gulp.task('browserify', function() {
  var stuff = _browserifier(paths.scripts.entry,
    paths.scripts.compiled, 'gis-layers-manager.js',{debug: true, standalone: 'gis-layers-manager'});
  return stuff.bundle();
});


gulp.task('clean', function(cb) {
  return rimraf('./dist/*', cb);
});


gulp.task('build-js', gulp.series('browserify', function() {
  return gulp.src(paths.scripts.compiled + 'gis-layers-manager.js')
    .pipe($.streamify($.uglify))
    .pipe(gulp.dest(paths.dist));
}));

gulp.task('less', function() {
  return gulp.src(paths.stylesheets.entry)
    .pipe($.plumber())
    .pipe($.sourcemaps.init())
    .pipe($.less())
    .on('error', log)
    .on('error', beep)
    .pipe($.sourcemaps.write())
    .pipe($.sourcemaps.init({ loadMaps: true }))
    .pipe($.concat('gis-layers-manager.css'))
    .pipe($.sourcemaps.write())
    .pipe(gulp.dest(paths.stylesheets.compiled));
});

gulp.task('build-css', gulp.series('less', function() {
	  return gulp.src(paths.stylesheets.compiled + 'gis-layers-manager.css')
	    .pipe($.csso())
	    .pipe(gulp.dest(paths.dist));
	}));

gulp.task('build-rootstuff', function() {
  return gulp.src(paths.rootStuff)
    .pipe(gulp.dest(paths.dist));
});


gulp.task('build', gulp.series('clean','build-js', gulp.parallel('build-css','build-rootstuff')));
