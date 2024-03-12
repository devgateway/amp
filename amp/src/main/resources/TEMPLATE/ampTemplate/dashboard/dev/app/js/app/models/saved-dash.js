var _ = require('underscore');
var BackboneDash = require('../backbone-dash');

module.exports = BackboneDash.Model.extend({

  defaults: {
    name: undefined,
    description: undefined,
    stateBlob: undefined
  },

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;
  }

}, {
  fromId: function(id, options) {
    return new this({ id: id }, options);
  }
});
