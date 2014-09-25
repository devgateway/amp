var _ = require('underscore');
var Backbone = require('backbone');

var originalSync = Backbone.sync;


// Eventually will be a config, but we don't have config built yet.
var _path = window.location.pathname,
    _CORS_API = 'http://66.207.103.134',
    _AMP_API = _path.substr(0, _path.indexOf('/TEMPLATE')),
    _IS_LOCAL = (window.location.host === 'localhost:3000'),
    API_BASE = _IS_LOCAL ? _CORS_API : _AMP_API;


Backbone.sync = function(method, model, options) {
  if (!model) {
    console.error('tried to sync without a model', method, model, options);
  }
  options = options || {};

  // calculate and append baseURL.
  var url = (typeof model.url === 'string') ? model.url : model.url(),
      fixedUrl = API_BASE + url;

  _.extend(options, {
    url: fixedUrl,
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    }
  });

  return originalSync.call(Backbone, method, model, options);
};
