var _ = require('underscore');
var StringUtils = {};

/**Extract string for current language from object that contains strings for several languages
 * If string for current language is not found it returns the string for the default AMP language.
 */
StringUtils.getMultilangString = function(layer, field, settings){  
	  var currentLanguage = settings.findWhere({id:'language'}).get('defaultId');
	  var defaultLanguage = settings.findWhere({id: 'default-language'}).get('defaultId');	    
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
