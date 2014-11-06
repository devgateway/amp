var _ = require('underscore');
var Backbone = require('backbone');


// TODO: ... ... ...
var IS_PHILS_CORS = window && (window.location.host === 'localhost:3000');


function InitError(instance) {
  this.instance = instance;
  this.toString = function() { return 'Module initialization error'; };
}


// we have to alias the old backbone sync here.
// otherwise things get messed up in filters init.
var bs = Backbone.sync;


function syncOverride(method, model, options) {
  var url = _.has(options, 'url') ? options.url : _.result(model, 'url');

  _(options).extend({
    // maybe use phil's DRC CORS dev server
    url: (IS_PHILS_CORS ? 'http://66.207.103.134' : '') + url,
    headers: {
      // jscs:disable disallowQuotedKeysInObjects
      'Accept': 'application/json',
      'Content-Type': 'application/json'
      // jscs:enable disallowQuotedKeysInObjects
    }
  });

  return bs.call(this, method, model, options);
}


// mixed into all models, collections and views to ensure we have a convenient
// reference to the app instance everywhere.
function mixDash(Class) {
  return Class.extend({

    constructor: function() {
      var constructed = Class.apply(this, arguments);
      if (!this.app) {
        throw new InitError(this);
      }
      return constructed;
    },

    sync: syncOverride

  });
}


module.exports = _({}).extend(Backbone, {
  // errors
  InitError: InitError,

  Model: mixDash(Backbone.Model),
  Collection: mixDash(Backbone.Collection),
  View: mixDash(Backbone.View),

  sync: syncOverride
});
