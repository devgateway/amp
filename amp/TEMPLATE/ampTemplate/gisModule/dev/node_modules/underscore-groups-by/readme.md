Underscore GroupsBy Mixin
=========================

A many-to-many version of [underscore's `_.groupBy`](http://underscorejs.org/#groupBy).


Install
-------

This module should work with plain old javascript, browserify, and requirejs.

For regular javascript, make sure you include it after underscore. Underscore is the only dependency.

For browserify and requirejs, you need to `require` it, but you don't need to do anything with the return value if you don't want -- it will mix itself into underscore for you.

### Install with npm

```bash
$ npm install groups-by
```


Browserify Example
------------------

```javascript

var _ = require('underscore');
require('underscore-groups-by');


var things = [
  {n: [1], name: 'alice'},
  {n: [2], name: 'bob'},
  {n: [1, 2], name: 'alob'}
];

var grouped = _(things).groupsBy(function (thing) {
  return thing.n;  // return value must be an array
});
```

logging the result...

```javascript
console.log(grouped);
```

should yield

```javascript
{
  1: [
    {n: [1], name: 'alice'},
    {n: [1, 2], name: 'alob'}
  ],
  2: [
    {n: [2], name: 'bob'},
    {n: [1, 2], name: 'alob'}
  ]
}
```


License
-------

MIT
