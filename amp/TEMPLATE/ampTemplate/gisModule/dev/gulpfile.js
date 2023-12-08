/*
 * AMP Frontend Gulpfile
 *
 * Usage:
 *
 * $ gulp
 * or
 * $ gulp dev-ci
 *    Unoptimized build of js/css, runs watchify and livereload, runs tests on every change, needs AMP server running.

 * $ gulp dev
 *    Unoptimized build of js/css, runs watchify and livereload, needs AMP server running.

 * $ gulp dev-mock
 *    Unoptimized build of js/css, with livereload, with mock-files.
 *
 * $ gulp test
 *    Run unit tests from command line
 *
 * $ gulp webtest
 *    Serve unit tests in browser
 *
 * $ gulp watch-and-test
 *    Runs tests, and re-runs tests when any of them changes. Good to run while writing tests.

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
var connect = require('gulp-connect');
const { createProxyMiddleware } = require('http-proxy-middleware');
var log = require('fancy-log');

var gulpi18nScraper = require('gulp-i18n-scraper');
var gulpUtil = require('gulp-util');
var mold = require('mold-source-map');
//var react = require('gulp-react');

var paths = {
  app: {
    root: './app',
    rootstuff: './app/*.{png,jpg,ico,html,txt,xml}',
    scripts: {
      top: './app/js/*.js',
      amp: './app/js/amp/**/*.js',
      entry: './app/js/amp/app.js',
      libs: './app/js/libs/',
      buildDest: './app/compiled-js/',
      built: './app/compiled-js/app.js'
    },
    templates: './app/js/**/*.html',
    stylesheets: {
      all: './app/less/**/*.less',
      entry: ['./app/less/main.less'],
      libs: ['./node_modules/leaflet/dist/**/*.css',
            './node_modules/leaflet.markercluster/dist/**/*.css',
            '../../node_modules/amp-filter/dist/amp-filter.css',
            '../../node_modules/gis-layers-manager/dist/gis-layers-manager.css',
            './app/js/libs/local/slider/**/*.css',
            '../../node_modules/amp-settings/dist/amp-settings.css'],
      compiledDest: './app/compiled-css/',
      compiled: './app/compiled-css/main.css'
    },
    mockAPI: {
      entry: './app/mock-api/fakeServer.js',
      compiledDest:'./app/compiled-js/',
      compiled:'./app/compiled-js/mock-api.js'
    },
    images: './app/img/**/*.{png,jpg,jpeg,gif,svg}',
    fonts: './app/fonts/**/*.{eot,svg,ttf,woff}'
  },
  dist: {
    root: '../dist/',  // USE CAUTION IF CHANGING (watch out for clean)
    scripts: '../dist/compiled-js/',
    stylesheets: '../dist/compiled-css/',
    images: '../dist/img/',
    fonts: '../dist/fonts/',
    mockAPI: '../dist/mock-api/'
  },
  tests: {
    root: './app/test',
    css: './node_modules/qunitjs/qunit/qunit.css',
    qunit: './node_modules/qunitjs/qunit/qunit.js',
    compiled: './app/test/compiled/compiled-test.js',
    compiledDest: './app/test/compiled/',
    entry: './app/test/entry.js',
    scripts: './app/test/tests/*.js'
  }
};


//------------------------------------
// helpers
//------------------------------------

function _bundlify(ifyer, entry, destFolder, destName) {
  var bundler = ifyer(entry);
  bundler.transform('brfs');

  var rebundle = function() {
    g.util.log('rebrowserifying ' + entry + '....');
    return bundler.bundle()
        .on('end', function() { log('bundle: finished'); })
        .on('error', function(e) { log('bundle: error: ', e); })
        .pipe(source(destName))
        .pipe(gulp.dest(destFolder));
  };
  bundler.on('update', rebundle);

  return rebundle();
}

// generate translate file
// TODO: figure out how to do elements that are created dynamically with templates...
gulp.task('translate',  function() {
  return gulp.src(paths.app.templates)
    .pipe(g.plumber())
    .pipe(gulpi18nScraper("*[data-i18n^='amp.']"))
      .on('error', g.util.log)
    .pipe(g.concat('i18n-mapping.json'))
    .pipe(gulp.dest(paths.app.scripts.buildDest));
});


gulp.task('bundle-mock',  function() {
  return _bundlify(browserify, paths.app.mockAPI.entry,
                   paths.app.mockAPI.compiledDest, 'mock-api.js');
});


gulp.task('watchify', function() {
  // recompile browserify modules
  return _bundlify(watchify, paths.app.scripts.entry,
                   paths.app.scripts.buildDest, 'app.js');
});


gulp.task('browserify', function() {
  return _bundlify(browserify, paths.app.scripts.entry,
                   paths.app.scripts.buildDest, 'app.js');
});


gulp.task('less', function() {
  return gulp.src(paths.app.stylesheets.libs.concat(paths.app.stylesheets.entry))
    .pipe(g.plumber())
    .pipe(g.less())
      .on('error', g.util.log)
      .on('error', g.util.beep)
    .pipe(g.concat('main.css'))
    .pipe(gulp.dest(paths.app.stylesheets.compiledDest));
});

const watchJsAndCss = cb => {
  gulp.watch([paths.app.scripts.top, paths.app.scripts.amp], gulp.series('lint'));
  gulp.watch(paths.app.stylesheets.all, gulp.series('less'));
  cb();
};

gulp.task('watch', gulp.series('watchify', watchJsAndCss));


gulp.task('lint', function() {
  return gulp.src([paths.app.scripts.amp, paths.app.scripts.top])
    .pipe(g.plumber())
    .pipe(g.jscs())
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


gulp.task('clean', function(done) {
  rimraf(paths.dist.root, done);
});




//------------------------------------
// build for dist
//------------------------------------

gulp.task('build-js', gulp.series('browserify', function() {
  return gulp.src(paths.app.scripts.built)
  	//.pipe(react())
    .pipe(g.streamify(g.uglify().on('error', gulpUtil.log)))
    .pipe(gulp.dest(paths.dist.scripts));
}));

gulp.task('build-css', gulp.series('less', function() {
  return gulp.src(paths.app.stylesheets.compiled)
    .pipe(g.csso())
    .pipe(gulp.dest(paths.dist.stylesheets));
}));

gulp.task('build-index',  function(){
  return gulp.src('app/src-index.html')
    .pipe(g.rename('index.html'))
    .pipe(gulp.dest('app/'));
});

const copyRootStuff = () => gulp.src(paths.app.rootstuff).pipe(gulp.dest(paths.dist.root));
const copyAppImages = () => gulp.src(paths.app.images).pipe(gulp.dest(paths.dist.images));
const copyAppFonts = () => gulp.src(paths.app.fonts).pipe(gulp.dest(paths.dist.fonts));
const copyEmptyJson = () => gulp.src('app/empty.json').pipe(gulp.dest(paths.dist.root));

gulp.task('copy-stuff', gulp.series('build-index',
  gulp.parallel(copyRootStuff, copyAppImages, copyAppFonts, copyEmptyJson)));

gulp.task('revision', gulp.series('clean', 'build-js', 'build-css', function() {
  var versionableGlob = '**/*.{js,css}';
  var antiHtmlFilter =  g.filter(versionableGlob);  // so we can avoid versioning html
  gulp.src([ paths.dist.root + versionableGlob,
             paths.dist.root + '**/*.html' ])
    .pipe(antiHtmlFilter)             // put the html aside for now
    .pipe(g.rimraf({ force: true }))  // remove the unversioned js/css from disk (they are still in the stream)
    .pipe(g.rev())                    // revision & update their filenames
    .pipe(antiHtmlFilter.restore())   // bring the html back into the stream
    .pipe(g.revReplace())             // replace asset names in html as revisioned names
    .pipe(gulp.dest(paths.dist.root));// write everything back to disk
}));

// TODO: fix revision for windows and re-enable
gulp.task('build', gulp.series('clean', 'build-js', 'build-css'/*, 'revision'*/, 'copy-stuff'));

gulp.task('preview', gulp.series('build', cb => {
  g.serve({
    root: [paths.dist.root],
    port: 3000
  })();
  cb();
}));




//------------------------------------
// dev
//------------------------------------
gulp.task('dev-server', cb => {
  connect.server({
    root: [ paths.app.root ],
    port: 3000,
    middleware: (connect, opt) => [
      createProxyMiddleware(['!/compiled-css/**', '!/compiled-js/**', '!/img/**', '!/fonts/**', '!/index.html'], {
        target: 'http://localhost:8080'
      })
    ],
  });
  cb();
});


gulp.task('reload', function(cb) {
  g.livereload.listen();
  gulp.watch([
    paths.app.rootstuff,
    paths.app.stylesheets.compiled,
    paths.app.scripts.built,
    paths.app.images,
    paths.app.fonts
  ]).on('change', g.livereload.changed);
  cb();
});


// takes src-index.html, injects mockAPI
gulp.task('dev-index', gulp.series('bundle-mock', function(){
  return gulp.src('app/src-index.html')
            .pipe(
                g.inject(
                  gulp.src(paths.app.mockAPI.compiled, {read:false}), {
                        addRootSlash: false,  // ensures proper relative paths
                        ignorePath: 'app/'  // makes sure relative path is correct
                    }))
            .pipe(g.rename('index.html'))
            .pipe(gulp.dest('app/'));
}));

// takes src-index.html, injects mockAPI
gulp.task('dev-index-no-mock', function(){
  return gulp.src('app/src-index.html')
            .pipe(g.rename('index.html'))
            .pipe(gulp.dest('app/'));
});

gulp.task('dev', gulp.series('lint', 'less', 'dev-server', 'watch', 'reload', 'dev-index-no-mock'));
gulp.task('dev-mock', gulp.series('lint', 'less', 'dev-server', 'watch', 'reload', 'dev-index'));



//------------------------------------
// test
//------------------------------------

gulp.task('build-tests', function() {
  gulp.src([paths.tests.css, paths.tests.qunit])
    .pipe(g.changed(paths.tests.compiledDest))
    .pipe(gulp.dest(paths.tests.compiledDest));
  return _bundlify(browserify, paths.tests.entry, paths.tests.compiledDest, 'compiled-test.js');
});

gulp.task('test', gulp.series('build-tests', function() {
  return gulp.src(paths.tests.entry, {read: false})
    .pipe(g.mocha());
}));

gulp.task('watch-and-test', gulp.series('test', function(cb) {
  // TODO: for some reason the watch on `paths.tests.scripts` doesn't work
  // when called from 'dev-ci' I think a different watch is overiding it....
  gulp.watch([paths.tests.scripts, paths.app.scripts.top, paths.app.scripts.amp], gulp.series('test'));
  cb();
}));

gulp.task('serve-tests', gulp.series('build-tests', cb => {
  g.serve({
    root: [paths.tests.root],
    port: 3000
  })();
  cb();
}));


gulp.task('reload-tests', gulp.series('build-tests', function(cb) {
  gulp.watch(paths.tests.scripts, gulp.series('build-tests'));
  gulp.watch(paths.tests.compiled)
    .on('change', g.livereload.changed);
  cb();
}));

gulp.task('webtest', gulp.series('build-tests', 'serve-tests', 'reload-tests'));
gulp.task('dev-ci', gulp.series('dev', 'watch-and-test'));
gulp.task('default', gulp.series('dev-ci'));
