var _ = require('underscore');
var Backbone = require('backbone');


// TODO: ... ... ...
var IS_PHILS_CORS = window && (window.location.host === 'localhost:3000');


var CACHE = ['/rest/dashboard'];


function InitError(instance) {
  this.instance = instance;
  this.toString = function() { return 'Module initialization error'; };
}


var syncOverride = (function(bs) {
  var cache = {};

  function _doSync(url, method, model, options) {
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

  function _cacheSync(url, method, model, options) {
    var key = url + '+POST:' + options.data;
    if (cache[key]) { return cache[key](); }

    var xhr = _doSync(url, method, model, options);

    cache[key] = function() {
      // calling this function will make the xhr re-call its callbacks
      return xhr
        .done(options.success)
        .always(options.complete);
    };

    // do not cache failed requests
    xhr.fail(function() { delete cache[key]; });

    return xhr;
  }

  return function(method, model, options) {
    var url = _.has(options, 'url') ? options.url : _.result(model, 'url'),
        cacheable = _.some(CACHE, function(i) { return url.indexOf(i) === 0; });
    return (cacheable ? _cacheSync : _doSync)(url, method, model, options);
  };
})(Backbone.sync);


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
