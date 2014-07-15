var _ = require('underscore');
var Backbone = require('backbone');


module.exports = Backbone.Model.extend({

  defaults: {
    name: undefined,
    description: undefined,
    stateBlob: undefined
  },

  serialize: function() {
    var stringified = window.JSON.stringify(this.get('stateBlob'));
    var encoded = window.encodeURIComponent(stringified);
    return encoded;
  }

}, {
  // class properties
  deserialize: function(serialized) {
    var unencoded = window.decodeURIComponent(serialized);
    var parsed = window.JSON.parse(unencoded);
    return parsed;
  }
});
