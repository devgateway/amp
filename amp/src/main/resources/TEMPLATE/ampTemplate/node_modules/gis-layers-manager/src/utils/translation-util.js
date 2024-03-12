var _ = require('underscore');
translationUtil = {};
translationUtil.availableLanguages = null;
translationUtil.getAvailableLanguages = function () {
		var deferred = $.Deferred();
	    if (translationUtil.availableLanguages) {
	      deferred.resolve(translationUtil.availableLanguages);
	    } else {
	      _fetchLanguages().then(function(languages) {
	    	translationUtil.availableLanguages =  _.pluck(languages, 'id');
	        deferred.resolve(translationUtil.availableLanguages);
	      });
	    }
	    return deferred;
		
};

function _fetchLanguages (){
	return jQuery.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		'type' : 'GET',
		'url' : '/rest/translations/multilingual-languages'
	});
};

module.exports = translationUtil;