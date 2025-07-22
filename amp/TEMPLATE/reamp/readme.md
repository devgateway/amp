# Reamp boilerplate
## Prerequisites
In order to be able to work efficiently you should familiarize yourself with the following technologies:

1. EcmaScript6 aka ES6 aka JS2015 aka Harmony.
The new version of JavaScript. Any valid JS is valid ES6, but ES6 brings to the table some new features
 so it won't hurt to [familiarize](http://es6-features.org/) yourself with it.
 _Note: some of these features(ex. constants) might require a polyfill in order to work in IE9._
 _For a quick and dirty solution try [Babel polyfill](https://babeljs.io/docs/usage/polyfill/)_
2. [React](http://facebook.github.io/react/)
3. [Jest](https://facebook.github.io/jest/) testing framework.
4. [Webpack](https://webpack.github.io/)
There's no need to dive deep into it, you'll mainly need to know [how to use loaders](http://webpack.github.io/docs/using-loaders.html)
and [polyfills](http://mts.io/2015/04/08/webpack-shims-polyfills/)

## Usage
Get Node.js

Install module's dependencies

    npm install

Note: At the moment we're using uglifyjs-webpack-plugin from a newer webpack version for compatibility with es6.
While installing you may need to use --legacy-peer-deps option.

Run

    npm start

Navigate to _dev.html's_ url. The browser will be automatically updated
	 whenever you save some changes. By default it will be deployed on port 3000.

To get the project ready for production, run just

    webpack

That will minify and optimize the scripts, use _index.html_ for production.
### AMP-UI development

TODO

### Debugging
Add the "debug" key to the local storage to get debug messages
```js
localStorage.debug = "*"
```
### Tests
Run

    npm test

### Organizing files
#### Module oriented
The goal is to group your files according to the component/module they belong to. If a module is trivial it can be just
one single file. If a module has submodules or assets, make it a folder. For this reason, do not specify the file
  extension when importing. To give an example, say you have a _components/my-module.es6_ file, and you require it like this:
```js
var MyModule = require('components/my-module.es6');
```

or
```js
import MyModule from 'components/my-module.es6';
```

Now suppose your module grew big enough it now deserves its own folder, but doing

    mkdir components/my-module && mv components/my-module.es6 components/my-module/index.es6

will make all of your imports break. That could've been avoided if you simply did not specify the file extension
 in the require/import statement.

#### Handling styles
Put your styles inside the component folder. Because of the [css-loader](https://github.com/webpack/css-loader)
and [less-loader](https://github.com/webpack/less-loader) you can just import those styles from JS, i.e.:
```js
require("./style.css")
```

or
```js
import HeaderStyle from "./style.css"
```

Styles will be bundled in index.js/index.min.js, also allowing hot reload during development.

If you have one single style file just name it _style.css_ or _style.less_, if you have complex styles better
put them in a "styles" folder and then require them individually
```js
require("./styles/fonts.css")
require("./styles/header.less")
require("./styles/theme.less")
```
#### Handling assets
If your module needs to read a config JSON, or load translations from a \*.po file, or any other type of asset,
 place the asset inside the module's folder, install the necessary WebPack loader, ex.

    npm install json-loader --save-dev

Modify _webpack.dev.config.js_ so that it uses the right loader for the extension. Then just require the asset from
 within your module:
```js
import config from "./config.json"
if(config.userUrl){
    //do stuff
} else {
    /do other stuff
}
```
#### Tests
Put your test cases inside a *\__tests__*(two underscores before the word _test_ and two after) folder within
 your module's folder.
