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
	  return result;  	
};

module.exports = StringUtils;
