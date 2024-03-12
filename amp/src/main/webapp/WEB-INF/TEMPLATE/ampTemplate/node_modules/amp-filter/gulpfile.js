var browserify = require('browserify');
var source = require('vinyl-source-stream');
var gulp = require('gulp');
var rimraf = require('rimraf');
var log = require('fancy-log');
var beep = require('beeper');
//var react = require('gulp-react');
var $ = require('gulp-load-plugins')();


var paths = {
  root: './src',
  ampRoot: '../../../../',
  rootStuff: './src/*.{png,jpg,ico,html,txt,xml}',
  dist: './dist/',
  scripts: {
    entry: './src/main.js',
    compiled: './src/compiled-js/',
    sources: [
      './src/models/*.js',
      './src/views/*.js',
      './src/tree/*.js',
      './src/lib/*.js',
      './gulpfile.js'
    ]
},
  stylesheets: {
    entry: './src/less/amp-filters.less',
    compiled: './src/compiled-css/',
    sources: ['./src/less/**/*.less'],
    testStyles: './bower_components/mocha/mocha.css'
  },
  img: {
    src: [
      './src/img/**'
    ],
    dest: './dist/img/'
  }
};

function _browserifier(entry, destFolder, destName, options) {
  options = options || {};
  options.entries = entry;
  var bundler = browserify(options);
  bundler.transform('brfs');
  /*bundler.external([
    'backbone',
    // don't need bootstrap here, as it just registers jQ plugins
    'jquery',
    'underscore'
  ]);*/

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
    paths.scripts.compiled, 'amp-filter.js',{debug: true, standalone: 'ampFilter'});
  return stuff.bundle();
});


gulp.task('clean', function(cb) {
  return rimraf(paths.dist, cb);
});


gulp.task('build-js', gulp.series('browserify', function() {
  return gulp.src(paths.scripts.compiled + 'amp-filter.js')
  	//.pipe(react())
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
    .pipe($.concat('amp-filter.css'))
    .pipe($.sourcemaps.write())
    .pipe(gulp.dest(paths.stylesheets.compiled));
});

gulp.task('build-css', gulp.series('less', function() {
  return gulp.src(paths.stylesheets.compiled + 'amp-filter.css')
    .pipe($.csso())
    .pipe(gulp.dest(paths.dist));
}));

gulp.task('images', function() {
  return gulp.src(paths.img.src)
    .pipe(gulp.dest(paths.img.dest));
});

gulp.task('build-static', gulp.series('images', function() {
  return gulp.src('./src/{fonts,img}/**/*')
    .pipe(gulp.dest(paths.dist));
}));

gulp.task('build-rootstuff', function() {
  return gulp.src(paths.rootStuff)
    .pipe(gulp.dest(paths.dist));
});

gulp.task('build', gulp.series('clean', 'build-js', gulp.parallel('build-css', 'build-static', 'build-rootstuff')));
