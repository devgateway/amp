var _ = require('underscore');
var Backbone = require('backbone');

module.exports = Backbone.Model.extend({

  defaults: {
    name: undefined,
    description: undefined,
    stateBlob: undefined
  },

  initialize: function(attrs, options) {
    this.appData = options.appData;
  },

  serialize: function() {
    return window.JSON.stringify(this.get('stateBlob'));
  }

}, {
  // class properties
  deserialize: function(serialized) {
    return window.JSON.parse(serialized);
  },

  fromId: function(id, options) {
    return new this({id: id}, options);
  }
});
