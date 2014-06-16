AMP GIS Module (Frontend)
=========================


Setup
-----

### Initial install

A node runtime with npm is required.

First, install `gulp` globally:

```bash
$ sudo npm install -g gulp
```

Then install all of the project dependencies locally:

```bash
$ npm install
```


### Updates

Call `npm install` after each pull to make sure latest plugins are there.

```bash
$ npm install
```


Usage
-----

All commands are automated with [gulp](http://gulpjs.com), and configured in
[gulpfile.js](gulpfile.js).


### Local development

#### Build, watch for changes, serve, and livereload:

```bash
$ gulp dev
```

The site will be available at [localhost:3000](http://localhost:3000)

Installing a [livereload browser extension](http://feedback.livereload.com/knowledgebase/articles/86242-how-do-i-install-and-use-the-browser-extensions-) is recommended.

#### Run tests

```bash
$ gulp test
```

#### Lint javascript and css sources

```bash
$ gulp lint
```

Javascript sources will also be linted when running `gulp dev`, check the terminal logs for issues.


### Production builds

Compiled versions for testing and distribution are in `dist/`.

```bash
$ gulp rev
```

or simply

```bash
$ gulp
```


Troubleshooting
---------------

### Build issues

**EADDRINUSE**: Probably have Node already running and port is already in use.

**Error: Cannot find module './app/js/main.js' from ...**: make sure you run gulp commands from the project root.

**JS Files not updating**: Try calling 'gulp clean' or make sure 'gulp dev' hasn't stopped.


Dev Reading
-----------

LESS: [lescss.org](http://lesscss.org/functions/)
