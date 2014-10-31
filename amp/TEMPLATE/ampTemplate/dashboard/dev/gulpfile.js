var browserify = require('browserify');
var watchify = require('watchify');
var source = require('vinyl-source-stream');
var gulp = require('gulp');
var g = require('gulp-load-plugins')();


var paths = {
  root: './app',
  rootStuff: './app/*.{png,jpg,ico,html,txt,xml}',
  scripts: {
    entry: './app/js/app.js',
    compiled: './app/compiled-js/',
    sources: [
      './app/js/**/*.js',
      '!./app/js/tests/run/**',  // skip compiled test stuff
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
      './bower_components/bootstrap/dist/css/bootstrap.css',
      './bower_components/nvd3/nv.d3.css',
      '../../node_modules/amp-filter/dist/amp-filter.css'
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
  var bundler = browserify(entry, options);

  var bundle = function() {
    g.util.log('bundle: start');
    return bundler.bundle()
      .on('end', function() { g.util.log('bundle: finished'); })
      .on('error', function(e) { g.util.log('bundle: error: ', e); })
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
    .pipe(g.plumber())
    .pipe(g.jscs())
    .pipe(g.jshint())
    .pipe(g.jshint.reporter('jshint-stylish'));
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


gulp.task('less', function() {
  return gulp.src(paths.stylesheets.entry)
    .pipe(g.plumber())
    .pipe(g.sourcemaps.init())
      .pipe(g.less())
        .on('error', g.util.log)
        .on('error', g.util.beep)
    .pipe(g.sourcemaps.write())
    .pipe(g.addSrc(paths.stylesheets.libs))
    .pipe(g.sourcemaps.init({ loadMaps: true }))
      .pipe(g.autoprefixer({ browsers: 'ie >= 9' }))
      .pipe(g.concat('main.css'))
    .pipe(g.sourcemaps.write())
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


gulp.task('serve', g.serve({
  root: [paths.root],
  port: 3000
}));


gulp.task('reload', ['watch'], function() {
  g.livereload.listen();
  return gulp.watch([
    paths.rootStuff,
    paths.scripts.compiled + 'app.js',
    paths.stylesheets.compiled + 'main.css'
  ]).on('change', g.livereload.changed);
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
    .pipe(g.mochaPhantomjs());
});


gulp.task('dev', ['watch', 'serve', 'reload']);
