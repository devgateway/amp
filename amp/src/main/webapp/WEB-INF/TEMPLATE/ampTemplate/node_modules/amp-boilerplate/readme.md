AMP Boilerplate
===============


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


### Production builds

Compiled versions for testing and distribution are in `dist/`. The amp-boilerplate.js version in dist 
is uglified. Under src/compiled-js/amp-boilerplate.js you can find the original, more readable, version

```bash
$ gulp build
```

*********************************************************************************************

For use with browserify (this way should be avoided. Gulp-ing should be the usual procedure):

### JavaScript

Broserify must be installed and on the `$PATH`:

```bash
$ browserify index.js -t brfs -s amp-boilerplate -o dist/amp-boilerplate.js
```



Anywhere in your app, do

```javascript
var boilerplate = require('amp-boilerplate');
```

Using in jsp:

<script type="text/javascript" src="<digi:file src="/TEMPLATE/ampTemplate/node_modules/amp-boilerplate/dist/amp-boilerplate.js"/>"></script>


