var _ = require('underscore');


// 'static' lib for helping work with the API.
module.exports = {

  // converts a filter object like this:
  // {adminLevel: "Region"}
  // into stringified, encoded, version of this:
  // {params: [{"filterName":"adminLevel","filterValue":["Region"]}]}
  convertToAMPStyle: function (filter){
    var paramsObj = { params:[]};

    //iterate over properties.
    _.each(filter, function(val, key){
      var tmpObj = {filterName: key, filterValue: val};  
      paramsObj.params.push(tmpObj);
    });

    // encode for api
    var string = encodeURI(JSON.stringify(paramsObj));
    return string;
  },


  getAPIBase: function(){
    var url = document.URL;
    return this._findBase(url);
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
