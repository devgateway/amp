var browserify = require('browserify');
var watchify = require('watchify');
var source = require('vinyl-source-stream');
var gulp = require('gulp');
var g = require('gulp-load-plugins')();


var paths = {
  app: {
    root: './app',
    rootStuff: './app/*.{png,jpg,ico,html,txt,xml}',
    scripts: {
      entry: './app/js/app.js',
      compiled: './app/compiled-js/',
      sources: ['./app/js/**/*.js', './gulpfile.js']
    },
    stylesheets: {
      entry: './app/less/main.less',
      compiled: './app/compiled-css/'
    }
  }
};


function _browserifier(entry, destFolder, destName, options) {
  var bundler = browserify(entry, options);
  bundler
    .transform('debowerify')
    .transform('brfs');

  var bundle = function() {
    g.util.log('bundling...');
    return bundler.bundle()
      .on('error', function(e) { g.util.log('bundling error: ', e); })
      .pipe(source(destName))
      .pipe(gulp.dest(destFolder));
  };

  return {
    bundler: bundler,
    bundle: bundle
  };
}


gulp.task('lint', function() {
  gulp.src(paths.app.scripts.sources)
    .pipe(g.plumber())
    .pipe(g.jscs())
    .pipe(g.jshint())
    .pipe(g.jshint.reporter('jshint-stylish'));
});


gulp.task('watchify', function() {
  // recompile browserify modules
  var stuff = _browserifier(paths.app.scripts.entry,
    paths.app.scripts.compiled, 'app.js', {
      // watchify options
      cache: {},
      packageCache: {},
      fullPaths: true,
      // browserify options
      debug: true
    });
  var bundler = watchify((stuff.bundler));
  bundler.on('update', stuff.bundle);
  return stuff.bundle();
});


gulp.task('browserify', function() {
  var stuff = _browserifier(paths.app.scripts.entry,
    paths.app.scripts.compiled, 'app.js');
  return stuff.bundle();
});


gulp.task('less', function() {
  return gulp.src(paths.app.stylesheets.entry)
    .pipe(g.plumber())
    .pipe(g.less())
      .on('error', g.util.log)
      .on('error', g.util.beep)
    .pipe(g.concat('main.css'))
    .pipe(gulp.dest(paths.app.stylesheets.compiled));
});


gulp.task('watch', ['watchify'], function() {
  gulp.watch([paths.app.scripts.sources], ['lint']);
});


gulp.task('serve', g.serve({
  root: [paths.app.root],
  port: 3000
}));


gulp.task('reload', ['watch'], function() {
  g.livereload.listen();
  return gulp.watch([
    paths.app.rootStuff,
    paths.app.stylesheets.compiled + 'main.css',
    paths.app.scripts.compiled + 'app.css'
  ]).on('change', g.livereload.changed);
});


gulp.task('dev', ['watch', 'serve', 'reload']);
