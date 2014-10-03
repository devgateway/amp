
Installation
------------

### Runtime requirements

NodeJS

### Dev requirements

Install the following, either with `sudo npm install -g` to use NPM directly, or with your operating system package manager:

 * `bower`
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


Build for production
--------------------

TO BE IMPLEMENTED IN THE GULPFILE

```
$ gulp build
```


Notes
-----

[`bower.json`](bower.json) lists an exact version of d3 that is fairly old, becuase nvd3 does not work with the latest versions. When nvd3 is fixed for this, we should update d3 to the latest.
