AMP Filters
===========

### Initial install

Install NodeJS and npm. http://nodejs.org/


Install `gulp` globally:

```bash
$ sudo npm install -g gulp
```

Install all of the package.json dependencies locally. Run this command from inside this folder.

```bash
$ npm install
```


### Updates

Call `npm install` after each pull to make sure latest build plugins are there.

```bash
$ npm install
```

When adding new dev/build dependencies, use the `--save-dev` option for npm so that they are added to [`package.json`](package.json), so that other developers can keep up to date. Make sure to commit the updated `package.json` to version control when you commit.

```bash
$ npm install jshint --save-dev
```


Usage
-----

All commands are automated with [gulp](http://gulpjs.com), and configured in [gulpfile.js](gulpfile.js).


#### Lint javascript and css sources

```bash
$ gulp lint
```

Javascript sources will also be linted when running `gulp dev`, check the terminal logs for issues.

Please always lint your code :)


### Production builds

Compiled versions for testing and distribution are in `dist/`. The amp-filter.js version in dist 
is uglified. Under src/compiled-js/amp-filter.js you can find the original, more readable, version

```bash
$ gulp build
```

*************************************************************************************

The older way of handling amp-filters (before gulp was introduced) is still available.

You can also still run it outside gulp by doing:
----------------------------------------

Broserify must be installed and on the `$PATH`:

```bash
$ browserify src/main.js -t brfs -s ampFilter -o dist/amp-filter.js
$ uglifyjs ./dist/amp-filter.js > ./dist/amp-filter.min.js
```

You can also use watchify with the same arguments, for development:

```bash
$ watchify src/main.js -t brfs -s ampFilter -o dist/amp-filter.js
```

### less

The lessc compiler must be installed and on the `$PATH`:

```bash
$ sudo npm install -g less
$ lessc src/less/amp-filters.less > dist/amp-filter.css
```

### Images

straight-forward:

```bash
$ rm -r dist/img && cp -r src/img dist/img
```
