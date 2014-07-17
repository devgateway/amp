/* test

    this._findBase('http://amp-210-dev-tc7.ampdev.net/TEMPLATE/ampTemplate/gisModule/dist/index.html');
    this._findBase('http://amp-210-dev-tc7.ampdev.net/TEMPLATE/ampTemplate/gisModule/dist/index.html');

*/


// Hacky temp lib for dynamically choosing right API route.
module.exports = {

  getAPIBase: function(){
    var url = document.URL;
    return this._findBase(url);
  },

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
