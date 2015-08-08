Nice Buckets!
=============

get human-friendly ranges


Install
-------

```bash
$ npm install nice-buckets
```


Usage
-----

```javascript
var niceBuckets = require('nice-buckets');

var buckets = 4,
    range: [3321.723, 78052.536];

console.log(buckets.minFigs(buckets, range));
```

yeilds the pleasing intervals:

```json
[ [ 0, 20000 ],
  [ 20000, 40000 ],
  [ 40000, 60000 ],
  [ 60000, 80000 ] ]
```


Versions
--------

This module is experimental -- it may start following semver later, but for now, every new version may break the api and the behavior.


...
---

The structure of this repo and some generic parts of the code are heavily inspired by boronine's [husl](https://github.com/boronine/husl).


License
-------

Copyright (C) 2014 Development Gateway

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
