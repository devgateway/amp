AMP GIS Module (Frontend)
=========================


Setup
-----

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


### Local development

#### Build, watch for changes, serve, and livereload:

```bash
$ gulp dev
```

or simply

```bash
$ gulp
```

Installing a [livereload browser extension](http://feedback.livereload.com/knowledgebase/articles/86242-how-do-i-install-and-use-the-browser-extensions-) is recommended.


#### Build, watch for changes, livereload with local AMP API:

1. Run AMP on eclipse.

2.

```bash
$ cd [amp-folder]/TEMPLATE/ampTemplate/gisModule/dev/
$ gulp dev
```

3. [//localhost:8080/TEMPLATE/ampTemplate/gisModule/dev/app/index.html](http://localhost:8080/TEMPLATE/ampTemplate/gisModule/dev/app/index.html)


#### Run tests

##### From the command line:

```bash
$ gulp test
```

The exit code will be `0` if all tests pass, or `1` if any fail.


##### In a web browser:

!Currently Disabled, used to work with qunit and phantomjs, since switch to mocha wasn't redone.!

```bash
$ gulp webtest
```

and then go to [localhost:3000](http://localhost:3000).


#### Lint javascript and css sources

```bash
$ gulp lint
```

Javascript sources will also be linted when running `gulp dev`, check the terminal logs for issues.

Please always lint your code :)


### Production builds

Compiled versions for testing and distribution are in `dist/`.

```bash
$ gulp build
```


Code conventions
----------------

### General style

Javascript coding style is somewhat enforced by jshint. The GIS module config can be found in [.jshintrc](.jshintrc), for which you may want to consult the [jshint config option docs](www.jshint.com/docs/options/).


### Backbone views


#### Templating

Never, ever write inline html in javascript. JSHint will unfortunately let this slide, but it will make me very sad.

Instead, use node's `fs.readFileSync` to load the html string from a template file.

```js
var fs = require('fs');
var _ = require('underscore');

// load the html string for the template
var Template = fs.readFileSync(path.join(__dirname, '../templates/template.html'))

// convert the string into an underscore template object:
var templater = _.template(myTemplate);

// when you need the html, call it:
var rawHTML = templater();

// see underscore templating docs for info about contexts, but:
var contextualHTML = templater({someTemplateVar: 'hello'});
```

Note that normally you have to use `dojo/text!`, but for the DG GIS front-end, we have aliased that to just `text!` in the AMD config for convenience.


#### Property ordering

In short:

 1. All static properties (variables, not functions) except `events` and `template`
 2. `events`, if present
 3. `template`, if present
 4. `initialize`, if present
 5. `render`, if present (should probably always be present)
 6. Any specialized render-like functions
 7. All other functions, ordered approximately by when they are expected to be called.


We treat `initialize` and `render` methods on views specially: they come before all other methods.

Everything that is not a function/method comes before `initialize`.


This is a good view:

```js
var MyView = Backbone.View.extend({

  hasGoodStyle: true,  // some arbitrary property

  template: _.template('<button>stylish</button>'),  // ew gross, inline html, don't do this please :)
                                                     // Use underscore templates in their own file!

  events: {
    'click button': 'checkStyle'
  }

  initialize: function() {
    // if I am present, I must be the first method here.
  },

  render: function() {
    // I always come after `initalize` but before everything else
  },

  checkStyle: function() {
    // all our custom functions down here
    console.log('have we got style or what?', this.hasGoodStyle);
  }

})
```

There is also a loose convention of pushing super-tiny helper functions down to the bottom. But just make your code nice :)


Troubleshooting
---------------

### Common build issues

**EADDRINUSE**: Probably have Node already running and port is already in use.

**Error: Cannot find module './app/js/main.js' from ...**: make sure you run gulp commands from the project root.

**JS Files not updating**: Try calling 'gulp clean' or make sure 'gulp dev' hasn't stopped.

**Windows Errors on install** Update your Node version: [nodejs.org/download/](http://nodejs.org/download/)


Dev Reading
-----------

### CSS

 * [lescss.org](http://lesscss.org/functions/)


### JS

 * [Backbone Fundamentals](http://addyosmani.github.io/backbone-fundamentals/) -- A thorough introduction to backbone.
 * [Human Javascript](http://read.humanjavascript.com/) -- An excellent overview of a maintainable backbone application structure.
 * [Composition and Inheritance](http://joostdevblog.blogspot.ca/2014/07/why-composition-is-often-better-than.html) -- some good ideas on when and when not to use inheritance.
