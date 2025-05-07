var _ = require('underscore');
var StringUtils = {};

/**Extract string for current language from object that contains strings for several languages
 * If string for current language is not found it returns the string for the default AMP language.
 */
StringUtils.getMultilangString = function(layer, field, generalSettings){  
	  var currentLanguage = generalSettings.get('language');
	  var defaultLanguage = generalSettings.get('default-language');	    
	  var result = '';
	  if(!_.isUndefined(layer[field])){
		  result = layer[field][currentLanguage];
		  if(_.isUndefined(result) || _.isNull(result)){
			  result = layer[field][defaultLanguage] || '';
		  } 
	  }
	  
	  // try to always return a value even if some data was missing at this point.
      try {
        if (!result) {
          console.warn('fallback to default string for field ' + field);
          if (layer[field] instanceof Object) {
            const keys = Object.keys(layer[field]);
            result = layer[field][keys[0]];
          } else {
            result = layer[field];
          }
        }
      } catch (e) {
	    console.error(e);
      }
	  if (result === null || result === undefined) {
	  	result = '';
	  }
	  
	  return result;  	
};

module.exports = StringUtils;
