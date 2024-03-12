var _ = require('underscore');
var Backbone = require('backbone');

var originalSync = Backbone.sync;


// Eventually will be a config, but we don't have config built yet.
var _path = window.location.pathname,
    _CORS_API = 'http://localhost:3000',
    _AMP_API = _path.substr(0, _path.indexOf('/TEMPLATE')),
    _IS_NODE = (window.location.host === 'localhost:3000'),
    API_BASE = _IS_NODE ? _CORS_API : _AMP_API;


function isRelative(url) {
  return !(new RegExp('^(?:[a-z]+:)?//', 'i')).test(url);
}


Backbone.sync = function(method, model, options) {
  if (!model) {
    console.error('tried to sync without a model', method, model, options);
  }
  options = options || {};

  // calculate and prepend baseURL.
  var url = (typeof model.url === 'string') ? model.url : model.url();
  if (isRelative(url)) {
    url = API_BASE + url;
  }

  _.extend(options, {
    url: url,
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    }
  });

  return originalSync.call(Backbone, method, model, options);
};
