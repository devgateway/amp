var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


var API_ID_KEY = 'mapId';


module.exports = BackboneDash.Model.extend({

  defaults: {
    name: undefined,
    description: undefined,
    stateBlob: undefined
  },

  // parse and toJSON map the id field to mapId for the API.
  parse: function(obj) {
    if (_(obj).has(API_ID_KEY)) {
      obj.id = obj[API_ID_KEY];
      delete obj[API_ID_KEY];
    }
    return obj;
  },

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;
  },

  toJSON: function() {
    var copy = BackboneDash.Model.prototype.toJSON.apply(this, arguments);
    if (_(copy).has('id')) {
      copy[API_ID_KEY] = copy.id;
      delete copy.id;
    }
    return copy;
  }

}, {
  fromId: function(id, options) {
    return new this({ id: id }, options);
  }
});
