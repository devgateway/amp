var runSequence = require('run-sequence');
var browserify = require('browserify');
var watchify = require('watchify');
var source = require('vinyl-source-stream');
var gulp = require('gulp');
var del = require('del');
//var react = require('gulp-react');
var $ = require('gulp-load-plugins')();


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
    $.util.log('bundle: start');
    return bundler.bundle()
      .on('end', function() { $.util.log('bundle: finished'); })
      .on('error', function(e) { $.util.log('bundle: error: ', e); })
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


gulp.task('clean', function() {
  return del(paths.dist, { force: true });
});


gulp.task('build-js', ['browserify'], function() {
  return gulp.src(paths.scripts.compiled + 'app.js')
    //.pipe(react())
    .pipe($.streamify($.uglify)) // CONSTANTIN - comment this one for development
    .pipe(gulp.dest(paths.dist + 'compiled-js/'));
});


gulp.task('build-css', ['less'], function() {
  return gulp.src(paths.stylesheets.compiled + 'main.css')
    .pipe($.csso())
    .pipe(gulp.dest(paths.dist + 'compiled-css/'));
});


gulp.task('build-static', ['images'], function() {
  return gulp.src('./app/{fonts,img}/**/*')
    .pipe(gulp.dest(paths.dist));
});

gulp.task('build-html', function() {
  return gulp.src('./app/index.html')
    .pipe(gulp.dest(paths.dist));
});


gulp.task('less', function() {
  return gulp.src(paths.stylesheets.entry)
    .pipe($.plumber())
    .pipe($.sourcemaps.init())
      .pipe($.less())
        .on('error', $.util.log)
        .on('error', $.util.beep)
    .pipe($.sourcemaps.write())
    .pipe($.addSrc.prepend(paths.stylesheets.libs))
    .pipe($.sourcemaps.init({ loadMaps: true }))
      .pipe($.autoprefixer({ browsers: 'ie >= 9' }))
      .pipe($.concat('main.css'))
    .pipe($.sourcemaps.write())
    .pipe(gulp.dest(paths.stylesheets.compiled));
});


gulp.task('images', function() {
  return gulp.src(paths.img.libs)
    .pipe(gulp.dest(paths.img.dest));
});


gulp.task('watch', ['watchify', 'lint', 'less', 'images'], function() {
  gulp.watch([paths.scripts.sources], ['lint']);
  gulp.watch(paths.stylesheets.sources, ['less']);
});


gulp.task('serve', $.serve({
  root: [paths.ampRoot],
  port: 3000
}));


gulp.task('reload', ['watch'], function() {
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


gulp.task('test', ['build-test-js', 'copy-test-resources'], function() {
  return gulp.src('app/js/tests/run/index.html')
    .pipe($.mochaPhantomjs());
});


gulp.task('dev', ['watch', 'serve', 'reload']);


gulp.task('build', function(cb) {
  runSequence('clean', 'build-js', 'build-css', 'build-static', 'build-html', cb);
});
