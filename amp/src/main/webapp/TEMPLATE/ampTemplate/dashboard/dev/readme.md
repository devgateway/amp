
Installation
------------

### Runtime requirements

NodeJS

### Dev requirements

Install the following, either with `sudo npm install -g` to use NPM directly, or with your operating system package manager:

 * `gulp`

If you use an IDE linter (like SublimeLiter), you will need these as well:

 * `jshint`
 * `jscs`

Note that they will be installed localy for `gulp`, so the project will still build without them.


Once those are in place, install the local dependencies

```bash
$ npm install
```


Development
-----------

Build everything, run a development server with livereload, with one easy command:

```
$ gulp dev
````

You can access the dashboards now at

[`localhost:3000/TEMPLATE/ampTemplate/dashboard/dev/app/index.html`](http://localhost:3000/TEMPLATE/ampTemplate/dashboard/dev/app/index.html)


Testing
-------

Tests run via a gulp tas:

```bash
$ gulp test
```

Gulp should return an appropriate code when running this task (zero for success, non-zero if any tests fail).


Build for production
--------------------

TO BE IMPLEMENTED IN THE GULPFILE

```
$ gulp build
```


Notes
-----

[`package.json`](package.json) lists an exact version of d3 that is fairly old, because nvd3 does not work with the latest versions. When nvd3 is fixed for this (or, optimisitcally, when we remove nvd3), we should update d3 to the latest.
