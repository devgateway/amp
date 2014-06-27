/*
 * AMP Frontend Gulpfile
 *
 * Usage:
 *
 * $ gulp rev
 *    Build app and revision assets
 *
 * $ gulp dev
 *    Un-optimized build, watch files, serve app, start livereload
 *
 * $ gulp test
 *    Run unit tests
 *
 * $ gulp lint
 *    Lint javascript and css
 *
 * $ gulp
 *    Default mode is `gulp rev` -- build and revision the app.
 */

var rimraf = require('rimraf');
var source = require('vinyl-source-stream');
var gulp = require('gulp');
var g = require('gulp-load-plugins')();


var paths = {
  app: {
    root: './app',
    rootstuff: './app/*.{png,ico,html,txt,xml}',
    scripts: {
      top: './app/js/*.js',
      amp: './app/js/amp/**/*.js',
      libs: './app/js/libs/',
      buildDest: './app/compiled-js/',
      built: './app/compiled-js/main.js'
    },
    stylesheets: {
      all: './app/less/**/*.less',
      entry: './app/less/main.less',
      compileDest: './app/compiled-css/',
      compiled: './app/compiled-css/main.css'
    },
    templates: './app/js/amp/**/*.html',
    images: './app/img/**/*.{png, jpg}',
    fonts: './app/fonts/**/*.{eot,svg,ttf,woff}'
  },
  dist: {
    dest: {
      root: '../dist/',  // USE CAUTION IF CHANGING
      scripts: '../dist/js/',
      stylesheets: '../dist/compiled-css/',
      images: '../dist/img/',
      fonts: '../dist/fonts/'
    }
  }
};
paths.devCompiled = [
  paths.app.stylesheets.compiled,
  paths.app.scripts.top,
  paths.app.scripts.amp,
  paths.app.templates,
  paths.app.images
];

// gulp.task('js', function() {
//   return gulp.src([paths.app.scripts.top, paths.app.scripts.amp])
//     .pipe(g.changed(paths.dist.scripts))
//     .pipe(gulp.dest(paths.dist.scripts));
// });


gulp.task('less', function() {
  return gulp.src(paths.app.stylesheets.entry)
    .pipe(g.plumber())
    .pipe(g.less())
      .on('error', g.util.log)
      .on('error', g.util.beep)
    .pipe(gulp.dest(paths.app.stylesheets.compileDest));
});


gulp.task('build-root', function() {
  return gulp.src(paths.app.rootstuff)
    .pipe(g.changed(paths.dist.dest.root))
    .pipe(gulp.dest(paths.dist.dest.root));
});


gulp.task('build-scripts', function() {
  return gulp.src(paths.app.scripts.built)
    .pipe(g.changed(paths.dist.dest.scripts))
    .pipe(gulp.dest(paths.dist.dest.scripts));
});


gulp.task('build-stylesheets', ['less'], function() {
  return gulp.src(paths.app.stylesheets.compiled)
    .pipe(g.changed(paths.dist.dest.stylesheets))
    .pipe(gulp.dest(paths.dist.dest.stylesheets));
});


gulp.task('build-images', function() {
  return gulp.src(paths.app.images)
    .pipe(g.changed(paths.dist.dest.images))
    .pipe(gulp.dest(paths.dist.dest.images));
});


gulp.task('build-fonts', function() {
  return gulp.src(paths.app.fonts)
    .pipe(g.changed(paths.dist.dest.fonts))
    .pipe(gulp.dest(paths.dist.dest.fonts));
});


gulp.task('build', ['build-root', 'build-scripts', 'build-stylesheets', 'build-images', 'build-fonts']);


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


gulp.task('serve', g.serve({
  root: [paths.app.root],
  port: 3000
}));


gulp.task('watch', function() {
  gulp.watch([paths.app.scripts.top, paths.app.scripts.amp], ['lint']);
  gulp.watch(paths.app.stylesheets.all, ['less']);
});


// gulp.task('reload', ['serve', 'watch'], function() {
//   g.livereload.listen();
//   return gulp.watch([paths.app.everything])
//     .on('change', g.livereload.changed);
// });

gulp.task('reload', ['serve', 'watch'], function() {
 g.livereload.listen();
 return gulp.watch(paths.devCompiled)
   .on('change', g.livereload.changed);
});


gulp.task('clean', function(done) {
  rimraf(paths.app.stylesheets.compiled, done);
});


// gulp.task('test', ['build', 'tests'], function() {
//   // copy tests and stuff (grossly) (TODO: don't copy from node_modules...)
//   // return gulp.src(paths.dist.tests + '/test-runner.html')
//   //   .pipe(g.qunit({verbose: true}));
// });




gulp.task('dev', ['lint', 'less', 'serve', 'watch', 'reload']);
