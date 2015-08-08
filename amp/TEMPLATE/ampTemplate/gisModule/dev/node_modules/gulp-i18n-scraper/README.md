gulp-i18n-scraper
=================

Scrapes template files for i18n keys and returns them and their values.

pass in the value you want to scrape for:` "*[data-i18n^='amp.']" `


Currently only supports buffers, not streams!


Example:
--------

```
gulp.task('translate',  function() {
  return gulp.src(paths.app.templates)
    .pipe(g.plumber())
    .pipe(gulpi18nScraper("*[data-i18n^='amp.']"))
      .on('error', g.util.log)
    .pipe(g.concat('i18n-mapping.json'))
    .pipe(gulp.dest(paths.app.scripts.buildDest));
});

```
