/*
 * AMP Frontend Gulpfile
 *
 * Usage:
 *
 * $ gulp
 * or
 * $ gulp dev
 *    Unoptimized build of js/css, runs a dev server on :3000 with livereload
 *
 * $ gulp test
 *    Run unit tests -- currently broken
 *
 * $ gulp lint
 *    Lint javascript and css -- currently only js
 *
 * $ gulp build
 *    Build, optimize, copy everything needed to ../dist, revision files.
 *
 * $ gulp preview
 *    Builds and then launches a local server on :3000 to check it out
 *
 */

var rimraf = require('rimraf');
var source = require('vinyl-source-stream');
var browserify = require('browserify');
var watchify = require('watchify');
var gulp = require('gulp');
var g = require('gulp-load-plugins')();


var paths = {
  app: {
    root: './app',
    rootstuff: './app/*.{png,ico,html,txt,xml}',
    scripts: {
      top: './app/js/*.js',
      amp: './app/js/amp/**/*.js',
      entry: './app/js/amp/main.js',
      libs: './app/js/libs/',
      buildDest: './app/compiled-js/',
      built: './app/compiled-js/main.js'
    },
    stylesheets: {
      all: './app/less/**/*.less',
      entry: './app/less/main.less',
      libs: './node_modules/leaflet/dist/**/*.css',
      compileDest: './app/compiled-css/',
      compiled: './app/compiled-css/main.css'
    },
    images: './app/img/**/*.{png, jpg}',
    fonts: './app/fonts/**/*.{eot,svg,ttf,woff}'
  },
  dist: {
    root: '../dist/',  // USE CAUTION IF CHANGING (watch out for clean)
    scripts: '../dist/compiled-js/',
    stylesheets: '../dist/compiled-css/',
    images: '../dist/img/',
    fonts: '../dist/fonts/'
  }
};


function _bundlify(ifyer) {
  var bundler = ifyer(paths.app.scripts.entry);
  bundler.transform('brfs');

  var rebundle = function() {
    g.util.log('rebrowserifying...');
    return bundler.bundle({debug: true})
      .on('error', function(e) { g.util.log('Browserify error: ', e); })
      .pipe(source('main.js'))
      .pipe(gulp.dest(paths.app.scripts.buildDest));
  };

  bundler.on('update', rebundle);

  return rebundle();
}


gulp.task('watchify', function() {
  // recompile browserify modules
  return _bundlify(watchify);
});


gulp.task('browserify', function() {
  return _bundlify(browserify);
});


gulp.task('less', function() {
  return gulp.src([paths.app.stylesheets.libs, paths.app.stylesheets.entry])
    .pipe(g.plumber())
    .pipe(g.less())
      .on('error', g.util.log)
      .on('error', g.util.beep)
    .pipe(g.concat('main.css'))
    .pipe(gulp.dest(paths.app.stylesheets.compileDest));
});


gulp.task('dev-server', g.serve({
  root: [paths.app.root],
  port: 3000
}));


gulp.task('watch', ['watchify'], function() {
  gulp.watch([paths.app.scripts.top, paths.app.scripts.amp], ['lint']);
  gulp.watch(paths.app.stylesheets.all, ['less']);
});


gulp.task('reload', ['dev-server', 'watch'], function() {
 g.livereload.listen();
 return gulp.watch([
    paths.app.rootstuff,
    paths.app.stylesheets.compiled,
    paths.app.scripts.built,
    paths.app.images,
    paths.app.fonts
  ]).on('change', g.livereload.changed);
});


gulp.task('clean', function(done) {
  rimraf(paths.dist.root, done);
});


gulp.task('build-js', ['clean', 'browserify'], function() {
  return gulp.src(paths.app.scripts.built)
    .pipe(g.streamify(g.uglify))
    .pipe(gulp.dest(paths.dist.scripts));
});


gulp.task('build-css', ['clean', 'less'], function() {
  return gulp.src(paths.app.stylesheets.compiled)
    .pipe(g.csso())
    .pipe(gulp.dest(paths.dist.stylesheets));
});


gulp.task('copy-stuff', ['clean'], function() {
  gulp.src(paths.app.rootstuff).pipe(gulp.dest(paths.dist.root));
  gulp.src(paths.app.images).pipe(gulp.dest(paths.dist.images));
  gulp.src(paths.app.fonts).pipe(gulp.dest(paths.dist.fonts));
});


gulp.task('revision', ['clean', 'build-js', 'build-css'], function() {
  var versionableGlob = '**/*.{js,css}'
  var antiHtmlFilter =  g.filter(versionableGlob)  // so we can avoid versioning html
  gulp.src([ paths.dist.root + versionableGlob,
             paths.dist.root + '**/*.html' ])
    .pipe(antiHtmlFilter)
    .pipe(g.rimraf({ force: true }))
    .pipe(g.rev())
    .pipe(gulp.dest(paths.dist.root))
    .pipe(antiHtmlFilter.restore())
    .pipe(g.revReplace())
    .pipe(gulp.dest(paths.dist.root))
});


gulp.task('lint', function() {
  gulp.src([paths.app.scripts.amp, paths.app.scripts.top])
    .pipe(g.jshint())
    .pipe(g.jshint.reporter('jshint-stylish'));

  // TODO: Lint CSS.
  //  * RECESS: Turns out bootstrap 3 dropped RECESS, which now fails.
  //  * csslint: kind of explodes on bootstrap's style... can it be made to
  //    ignore bootstrap stuff?
  //  * csscomb: not a linter so much as a property re-organizer; bootstrap now
  //    uses -- blog.getboostrap.com/2014/01/30/boostrap-3-1-0-released
  //    Unclear exactly how csscomb would fit in.
});


gulp.task('test', ['build'], function() {
  // TODO...
});


gulp.task('preview', ['build'], g.serve({
  root: [paths.dist.root],
  port: 3000
}));


gulp.task('dev', ['lint', 'less', 'dev-server', 'watch', 'reload']);
gulp.task('build', ['clean', 'build-js', 'build-css', 'copy-stuff', 'revision']);

gulp.task('default', ['dev']);
