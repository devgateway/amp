var _ = require('underscore');
var Backbone = require('backbone');


var API_ID_KEY = 'mapId';


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
  },

  // parse and toJSON map the id field to mapId for the API.
  parse: function(obj) {
    if (_(obj).has(API_ID_KEY)) {
      obj.id = obj[API_ID_KEY];
      delete obj[API_ID_KEY];
    }
    return obj;
  },

  toJSON: function() {
    var copy = Backbone.Model.prototype.toJSON.apply(this, arguments);
    if (_(copy).has('id')) {
      copy[API_ID_KEY] = copy.id;
      delete copy.id;
    }
    return copy;
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
