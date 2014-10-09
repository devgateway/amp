var _ = require('underscore');
var Backbone = require('backbone');


function InitError(instance) {
  this.instance = instance;
  this.toString = function() { return 'Module initialization error'; };
}

// mixed into all models, collections and views to ensure we have a convenient
// reference to the app instance everywhere.
function enforceAppRef(Class) {
  return Class.extend({

    constructor: function() {
      var constructed = Class.apply(this, arguments);
      if (!_(this).has('app')) {
        throw new InitError(this);
      }
      return constructed;
    }

  });
}


function syncOverride(method, model, options) {
  var url = _.has(options, 'url') ? options.url : _.result(model, 'url');
  _(options).extend({
    url: 'http://66.207.103.134' + url,
    headers: {
      // jscs:disable disallowQuotedKeysInObjects
      'Accept': 'application/json',
      'Content-Type': 'application/json'
      // jscs:enable disallowQuotedKeysInObjects
    }
  });
  return Backbone.sync.call(this, method, model, options);
}


module.exports = _({}).extend(Backbone, {
  // errors
  InitError: InitError,

  Model: enforceAppRef(Backbone.Model),
  Collection: enforceAppRef(Backbone.Collection),
  View: enforceAppRef(Backbone.View),

  sync: syncOverride
});
