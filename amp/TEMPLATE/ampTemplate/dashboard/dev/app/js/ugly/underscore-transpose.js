var _ = require('underscore');

_.mixin({
  transpose: function(arrs) {
    return _.zip.apply(_, arrs);
  }
});
