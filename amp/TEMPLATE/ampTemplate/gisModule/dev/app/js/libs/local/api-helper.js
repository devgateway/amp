var _ = require('underscore');


// 'static' lib for helping work with the API.
module.exports = {

  getAPIBase: function(){
    if (typeof document !== 'undefined') {
      var url = document.URL;
      return this._findBase(url);
    } else {
      console.warn('not found');
      return ''
    }
  },

  // Hacky way of finding the 'base' url for the api.
  // Eventually will be a config, but we don't have config built yet.
  _findBase: function(url){
    var baseURL = '';

    if(url.indexOf('http://localhost:3000') > -1){
      baseURL = '';
    } else if(url.indexOf('/TEMPLATE') > -1){
      var endOfBase = url.indexOf('/TEMPLATE');
      var baseURL = url.substr(0, endOfBase);
    }

    return baseURL;
  }
}
