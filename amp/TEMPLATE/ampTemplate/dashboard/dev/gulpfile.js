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
      './gulpfile.js'
    ]
  },
  stylesheets: {
    entry: './app/less/main.less',
    compiled: './app/compiled-css/',
    sources: ['./app/less/**/*.less'],
    libs: [
      './bower_components/bootstrap/dist/css/bootstrap.css'
    ]
  }
};


function _browserifier(entry, destFolder, destName, options) {
  var bundler = browserify(entry, options);
  bundler
    .transform('debowerify')
    .transform('brfs');

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


gulp.task('watch', ['watchify', 'lint', 'less'], function() {
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


gulp.task('dev', ['watch', 'serve', 'reload']);
