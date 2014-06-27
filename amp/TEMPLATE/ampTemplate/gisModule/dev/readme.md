AMP GIS Module (Frontend)
=========================


Setup
-----

### Initial install

A node runtime with npm is required.

First, install `gulp` and `bower` globally:

```bash
$ sudo npm install -g gulp bower
```

Next grab the front-end libraries
```bash
$ bower install
```

Finally, install all of the build dependencies locally:

```bash
$ npm install
```


### Updates

#### Dev & build stuff

Call `npm install` after each pull to make sure latest build plugins are there.

```bash
$ npm install
```

When installing new dev/build dependencies, use the `--save-dev` option for npm so that they are added to [`package.json`](package.json), so that other developers can keep up to date. Make sure to commit the updated `package.json` to version control when you commit.

```bash
$ npm install jshint --save-dev
```

#### Front end libraries

Keeping up to date is almost the same as for build/dev stuff:

```bash
$ bower install
```

Adding new libraries takes a few more steps.

 1. I (phil) think there is an equivalent to `--save-dev` for bower, but I have just been updating [`bower.json`](bower.json) manually as new libraries come in. I do `bower install jquery`, and then add the entry, checking the version number that bower reports having installed.

 2. Configure the package for AMD in [`app/js/config.js`](app/js/config.js), so that the new library can be conveniently listed as a dependency in `define` calls.


Usage
-----

All commands are automated with [gulp](http://gulpjs.com), and configured in [gulpfile.js](gulpfile.js).


### Local development

#### Build, watch for changes, serve, and livereload:

```bash
$ gulp dev
```

The site will be available at [localhost:3000](http://localhost:3000)

Installing a [livereload browser extension](http://feedback.livereload.com/knowledgebase/articles/86242-how-do-i-install-and-use-the-browser-extensions-) is recommended.

#### Run tests

Command-line tests are currently **not working**:

> ```bash
> $ gulp test
> ```

**Instead**, run the tests in-browser:

 1. Build the site
    ```bash
    $ gulp build
    ```
 2. Copy the testing scripts
    ```bash
    $ gulp tests
    ```
    _note plural `tests`, **not** `test` singular, which tries to run them_
 3. Serve the site locally
    ```bash
    $ gulp serve
    ```
    And then browse to the testing page: [localhost:3000/test/](http://localhost:3000/test/)

#### Lint javascript and css sources

```bash
$ gulp lint
```

Javascript sources will also be linted when running `gulp dev`, check the terminal logs for issues.

Please always lint your code :)


### Production builds

Compiled versions for testing and distribution are in `dist/`.

```bash
$ gulp rev
```

or simply

```bash
$ gulp
```


Code conventions
----------------

### General style

Javascript coding style is somewhat enforced by jshint. The GIS module config can be found in [.jshintrc](.jshintrc), for which you may want to consult the [jshint config option docs](www.jshint.com/docs/options/).


### Backbone views


#### Templating

Never, ever write inline html in javascript. JSHint will unfortunately let this slide, but it will make me very sad.

Instead, use the dojo `text!` plugin to require the html string from a template file with AMD.

```js
define(['underscore', 'text!my-template.html'], function(_, myTemplate) {

  // convert the string into an underscore template object:
  var templater = _.template(myTemplate);

  // when you need the html, call it:
  var rawHTML = templater();

  // see underscore templating docs for info about contexts, but:
  var contextualHTML = templater({someTemplateVar: 'hello'});

});
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
 7. All other functions, ordered approximately by when they are expected t be called.


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


Dev Reading
-----------

LESS: [lescss.org](http://lesscss.org/functions/)
