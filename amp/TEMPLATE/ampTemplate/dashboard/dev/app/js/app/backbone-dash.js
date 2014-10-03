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


function syncOverride() {
  return this.__super__.sync.apply(this, arguments);
}


module.exports = _(Backbone).extend({
  // errors
  InitError: InitError,

  Model: enforceAppRef(Backbone.Model),
  Collection: enforceAppRef(Backbone.Collection),
  View: enforceAppRef(Backbone.View),

  sync: syncOverride
});
