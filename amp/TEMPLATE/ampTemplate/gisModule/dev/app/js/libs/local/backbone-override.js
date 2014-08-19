var Backbone = require('backbone');

var originalSync = Backbone.sync;

// Hacky way of finding the 'base' url for the api.
// Eventually will be a config, but we don't have config built yet.
var _findBase = function(url){
  var baseURL = '';

  if(url.indexOf('http://localhost:3000') > -1){
    baseURL = '';
  } else if(url.indexOf('/TEMPLATE') > -1){
    var endOfBase = url.indexOf('/TEMPLATE');
    baseURL = url.substr(0, endOfBase);
  }

  return baseURL;
};

var _getAPIBase = function(){
  if (typeof document !== 'undefined') {
    var url = document.URL;
    return _findBase(url);
  } else {
    console.warn('not found');
    return '';
  }
};


Backbone.sync = function (method, model, options) {
  options = options || {};


  options.headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
  };

  // calculate and append baseURL.
  if(model && model.url){
    // Note a url can be either a string, or a function.
    if( typeof model.url === 'string'){
      model.url = _getAPIBase() + model.url;
    } else{
      model.url = _getAPIBase() + model.url();
    }
  }

  return originalSync.apply(Backbone, arguments);
};
