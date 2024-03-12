var browserify = require('browserify');
var watchify = require('watchify');
var source = require('vinyl-source-stream');
var gulp = require('gulp');
//var react = require('gulp-react');
var $ = require('gulp-load-plugins')();
var log = require('fancy-log');
var beep = require('beeper');
var rimraf = require('rimraf');

var paths = {
  root: './app',
  ampRoot: '../../../../',
  rootStuff: './app/*.{png,jpg,ico,html,txt,xml}',
  dist: '../build/',
  scripts: {
    entry: './app/js/app.js',
    compiled: './app/compiled-js/',
    sources: [
      './app/index.html',
      './app/js/**/*.js',
      '!./app/js/tests/run/**',  // skip compiled test stuff
      '!./app/js/ugly/lib-src/**',  // skip hard-copied external sources
      './gulpfile.js'
    ],
    test: './app/js/tests/test.js',
    testLib: './bower_components/mocha/mocha.js',
    testDest: './app/js/tests/run/'
  },
  stylesheets: {
    entry: './app/less/main.less',
    compiled: './app/compiled-css/',
    sources: ['./app/less/**/*.less'],
    libs: [
      './node_modules/bootstrap/dist/css/bootstrap.css',
      './node_modules/nvd3/build/nv.d3.css',
      '../../node_modules/amp-filter/dist/amp-filter.css',
      '../../node_modules/amp-settings/dist/amp-settings.css'
    ],
    testStyles: './bower_components/mocha/mocha.css'
  },
  img: {
    libs: [
      '../../node_modules/amp-filter/dist/img/**'
    ],
    dest: './app/img/'
  },
  testPage: './app/js/tests/index.html'
};


function _browserifier(entry, destFolder, destName, options) {
  
  //TODO: Constantin: understand the build process better before making a nonbreaking change
  options = options || {};
  options.entries = entry;
  var bundler = browserify(options);
  
  //var bundler = browserify(entry, options);
  bundler.external([
    'backbone',
    // don't need bootstrap here, as it just registers jQ plugins
    'd3',
    'jquery',
    'underscore'
  ]);

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


gulp.task('watchify', function() {
  // recompile browserify modules
  var stuff = _browserifier(paths.scripts.entry,
    paths.scripts.compiled, 'app.js', {
      // watchify options
      cache: {},
      packageCache: {},
      fullPaths: true,
      // browserify options
      debug: true
    });
  var bundler = watchify(stuff.bundler);
  bundler.on('update', stuff.bundle);
  return stuff.bundle();
});


gulp.task('browserify', function() {
  var stuff = _browserifier(paths.scripts.entry,
    paths.scripts.compiled, 'app.js');
  return stuff.bundle();
});


gulp.task('clean', function(cb) {
  rimraf(paths.dist, cb);
});


gulp.task('build-js', gulp.series('browserify', function() {
  return gulp.src(paths.scripts.compiled + 'app.js')
    //.pipe(react())
    .pipe($.streamify($.uglify)) // CONSTANTIN - comment this one for development
    .pipe(gulp.dest(paths.dist + 'compiled-js/'));
}));

gulp.task('less', function() {
  return gulp.src(paths.stylesheets.entry)
    .pipe($.plumber())
    .pipe($.sourcemaps.init())
    .pipe($.less())
    .on('error', log)
    .on('error', beep)
    .pipe($.sourcemaps.write())
    .pipe($.addSrc.prepend(paths.stylesheets.libs))
    .pipe($.sourcemaps.init({ loadMaps: true }))
    .pipe($.autoprefixer())
    .pipe($.concat('main.css'))
    .pipe($.sourcemaps.write())
    .pipe(gulp.dest(paths.stylesheets.compiled));
});

gulp.task('build-css', gulp.series('less', function() {
  return gulp.src(paths.stylesheets.compiled + 'main.css')
    .pipe($.csso())
    .pipe(gulp.dest(paths.dist + 'compiled-css/'));
}));

gulp.task('images', function() {
  return gulp.src(paths.img.libs)
    .pipe(gulp.dest(paths.img.dest));
});

gulp.task('build-static', gulp.series('images', function() {
  return gulp.src('./app/{fonts,img}/**/*')
    .pipe(gulp.dest(paths.dist));
}));

gulp.task('build-html', function() {
  return gulp.src('./app/index.html')
    .pipe(gulp.dest(paths.dist));
});

const watchJsAndCss = cb => {
  gulp.watch(paths.scripts.sources, gulp.series('lint'));
  gulp.watch(paths.stylesheets.sources, gulp.series('less'));
  cb();
}

gulp.task('watch', gulp.series('watchify', 'lint', 'less', 'images', watchJsAndCss));


gulp.task('serve', cb => {
  $.serve({
    root: [paths.ampRoot],
    port: 3000
  })();
  cb();
});


gulp.task('reload', function() {
  $.livereload.listen();
  return gulp.watch([
    paths.rootStuff,
    paths.scripts.compiled + 'app.js',
    paths.stylesheets.compiled + 'main.css'
  ]).on('change', $.livereload.changed);
});


gulp.task('build-test-js', function() {
  var stuff = _browserifier(paths.scripts.test,
    paths.scripts.testDest, 'test.js');
  return stuff.bundle();
});


gulp.task('copy-test-resources', function() {
  return gulp.src([
    paths.scripts.testLib,
    paths.stylesheets.testStyles,
    paths.testPage
  ])
    .pipe(gulp.dest(paths.scripts.testDest))
    .pipe(gulp.src(paths.img.libs))
    .pipe(gulp.dest(paths.scripts.testDest + 'img/'));
});


gulp.task('test', gulp.series('build-test-js', 'copy-test-resources', function() {
  return gulp.src('app/js/tests/run/index.html')
    .pipe($.mochaPhantomjs());
}));


gulp.task('dev', gulp.series('watch', 'serve', 'reload'));


gulp.task('build', gulp.series('clean', 'build-js', 'build-css', 'build-static', 'build-html'));
