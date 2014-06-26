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

var gulp = require('gulp');
var source = require('vinyl-source-stream');
var g = require('gulp-load-plugins')();


var paths = {
  app: {
    rootstuff: './app/*.{png,ico,html,txt,xml}',
    scripts: {
      amp: './app/js/amp/**/*.js',
      top: './app/js/*.js',
      all: './app/js/**/*.js'
    },
    templates: './app/js/**/*.{html,htm}',
    stylesheets: {
      entry: './app/less/main.less',
      all: './app/less/**/*.less'
    },
    images: './app/img/**/*.{png, jpg}',
    fonts: './app/fonts/**/*.{eot,svg,ttf,woff}',
    tests: [
      './app/test/**',
      './app/js/libs/vendor/qunit/qunit/qunit.css'
    ]
  },
  dist: {
    root: '../dist',  // USE CAUTION IF CHANGING
    scripts: '../dist/js',
    templates: '../dist/js',
    stylesheets: '../dist/css',
    images: '../dist/img',
    fonts: '../dist/fonts',
    tests: '../dist/test'
  }
};


gulp.task('rootstuff', function() {
  return gulp.src(paths.app.rootstuff)
    .pipe(g.changed(paths.dist.root))
    .pipe(gulp.dest(paths.dist.root));
});


gulp.task('js', function() {
  return gulp.src(paths.app.scripts.all)
    .pipe(g.changed(paths.dist.scripts))
    .pipe(gulp.dest(paths.dist.scripts));
});


gulp.task('css', function() {
  return gulp.src(paths.app.stylesheets.entry)
    .pipe(g.plumber())
    .pipe(g.less())
      .on('error', g.util.log)
      .on('error', g.util.beep)
    //.pipe(g.csso())
    .pipe(gulp.dest(paths.dist.stylesheets));
});

gulp.task('templates', function() {
  return gulp.src(paths.app.templates)
    .pipe(g.changed(paths.dist.templates))
    .pipe(gulp.dest(paths.dist.templates));
});

gulp.task('img', function() {
  return gulp.src(paths.app.images)
    .pipe(g.changed(paths.dist.images))
    .pipe(gulp.dest(paths.dist.images));
});

gulp.task('fonts', function() {
  return gulp.src(paths.app.fonts)
    .pipe(g.changed(paths.dist.fonts))
    .pipe(gulp.dest(paths.dist.fonts));
});


gulp.task('tests', function() {
  // simply copies the test files over, see the "test" task that runs them.
  return gulp.src(paths.app.tests)
    .pipe(g.changed(paths.dist.tests))
    .pipe(gulp.dest(paths.dist.tests))
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


gulp.task('serve', ['build'], g.serve({
  root: [paths.dist.root],
  port: 3000
}));


gulp.task('watch', ['build'], function() {
  gulp.watch(paths.app.rootstuff, ['rootstuff']);
  gulp.watch(paths.app.scripts.all, ['js', 'lint']);
  gulp.watch(paths.app.stylesheets.all, ['css']);
  gulp.watch(paths.app.images, ['img']);
  gulp.watch(paths.app.fonts, ['fonts']);
  gulp.watch(paths.app.templates, ['templates']);
});


gulp.task('reload', ['serve', 'watch'], function() {
  g.livereload.listen();
  return gulp.watch(paths.dist.root + '/**')
    .on('change', g.livereload.changed)
});


gulp.task('clean', function() {
  return gulp.src(paths.dist.root, {read: false})
    .pipe(g.clean({force: true}));
});


gulp.task('test', ['build', 'tests'], function() {
  // copy tests and stuff (grossly) (TODO: don't copy from node_modules...)
  return gulp.src(paths.dist.tests + '/test-runner.html')
    .pipe(g.qunit({verbose: true}));
});


gulp.task('build', ['rootstuff', 'js', 'templates', 'css', 'img', 'fonts']);


gulp.task('dev', ['build', 'serve', 'watch', 'reload']);
gulp.task('rev', ['build'], function() {
  // var dontVersion = [
  //  // /.*\.html$/g,  // skip templates
  //   /^[^\/]+?\.(png|ico|txt|xml)$/  // skip rootstuff
  // ];
  // return gulp.src(paths.dist.root + '/**')
  //   .pipe(g.revAll({ignore: dontVersion}))
  //   .pipe(gulp.dest(paths.dist.root));
});

// Run gulp with no command to build and revision
gulp.task('default', ['rev']);
