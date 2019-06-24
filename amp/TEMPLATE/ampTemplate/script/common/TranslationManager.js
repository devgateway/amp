var TranslationManager = {
		globalTranslationCache : {}
};
"use strict";

//var prefix = 'tabs.common:';
var prefix = '';
var availableLanguages = null;

var easternToWesternArabicSymbolMap = {
    '1': '١',
    '2': '٢',
    '3': '٣',
    '4': '٤',
    '5': '٥',
    '6': '٦',
    '7': '٧',
    '8': '٨',
    '9': '٩',
    '0': '٠'
}

var westernToEasternArabicSymbolMap = {
    '١': '1',
    '٢': '2',
    '٣': '3',
    '٤': '4',
    '٥': '5',
    '٦': '6',
    '٧': '7',
    '٨': '8',
    '٩': '9',
    '٠': '0'
}

function TranslationManager() {
	if (!(this instanceof TranslationManager)) {
		throw new TypeError("TranslationManager constructor cannot be called as a function.");
	}
	getAvailableLanguages ();
}

TranslationManager.prototype = {
	constructor : TranslationManager
};

function initializeGlobalTranslationsCache() {
//	if (app.TabsApp.globalTranslationCache === undefined || app.TabsApp.globalTranslationCache === null) {
//		app.TabsApp.globalTranslationCache = {};
//	}
}

function putTranslationValueInCache(key, value) {
	key = key.replace(/['"]/ig, ' '); //Sanitize some strings in french that would break the js later.
	TranslationManager.globalTranslationCache[key] = value;
}


function lookForTranslationByKey(key) {
	var translation = TranslationManager.globalTranslationCache[key];
	if (translation) {
		// console.log('found translation: ' + key);
		return translation;
	} else {
		return null;
	}
}

TranslationManager.convertNumbersToEasternArabic = function(input) {
    return input.replace(/\d/g, function (match) {
        return easternToWesternArabicSymbolMap[match];
    }).replace(/,/g, '،');
}

TranslationManager.convertNumbersToWesternArabic = function(input) {
    return input.replace(/[٠١٢٣٤٥٦٧٨٩]/g, function (match) {
        return westernToEasternArabicSymbolMap[match];
    }).replace(/,/g, '،');
}

TranslationManager.convertNumbersToEasternArabicIfNeeded = function(isRtl, language, region, input) {
    if (isRtl && language === "ar" && region === "EG") {
        return TranslationManager.convertNumbersToEasternArabic(input);
    }

    return input;
}

TranslationManager.convertNumbersToWesternArabicIfNeeded = function(isRtl, language, region, input) {
    if (isRtl && language === "ar" && region === "EG") {
        return TranslationManager.convertNumbersToWesternArabic(input);
    }

    return input;
}


TranslationManager.getAvailableLanguages = function () {
	var deferred = $.Deferred();
    if (availableLanguages) {
      deferred.resolve(availableLanguages);
    } else {
      _fetchLanguages().then(function(languages) {
    	availableLanguages =  _.pluck(languages, 'id');
        deferred.resolve(availableLanguages);
      });
    }

    return deferred;

}
function _fetchLanguages (){
return jQuery.ajax({
	headers : {
		'Accept' : 'application/json',
		'Content-Type' : 'application/json'
	},
	'type' : 'GET',
	'url' : '/rest/translations/multilingual-languages'
});
}


TranslationManager.searchAndTranslate = function() {
	initializeGlobalTranslationsCache();

	var postit = {};
	_.each($("*[data-i18n^='tabs.']"), function(i, val) {
		var key = $(i).attr('data-i18n');
		var value = $(i).text();
		var translationFromCache = lookForTranslationByKey(key);
		if (translationFromCache === null) {
			postit[key] = value;
		}
	});
	// Do not call endpoint when there is nothing to translate to save some
	// extra ms (500ms in Timor!).
	if (!jQuery.isEmptyObject(postit)) {
		TranslationManager.postJSON('/rest/translations/label-translations', postit, function(data) {
			$.each(data, function(key, value) {
				putTranslationValueInCache(key, value);
			});
			$.each(TranslationManager.globalTranslationCache, function(key, value) {
				$("*[data-i18n='" + key + "']").text(value);
			});
		});
	} else {
		$.each(TranslationManager.globalTranslationCache, function(key, value) {
			$("*[data-i18n='" + key + "']").text(value);
		});
	}
};


TranslationManager.getTranslated = function (text) {
    initializeGlobalTranslationsCache();
    var translationFromCache = lookForTranslationByKey(prefix + text);
    if (translationFromCache !== null) {
        return translationFromCache;
    } else {
        var textObject = {};
        var key = prefix + text;
        if (text instanceof Array) {
            $.each(text, function (key, value) {
                textObject[value] = value;
            });
        } else {
            textObject[key] = text;
        }
        var response = (TranslationManager.postJSON('/rest/translations/label-translations',
                textObject, function (data) {
                    //console.log(data);
                }
            )
        );
        putTranslationValueInCache(key, response.responseJSON[key]);
        if (text instanceof Array) {
            return response.responseJSON;
        } else {
            return response.responseJSON[key];
        }
    }
};

TranslationManager.postJSON = function (url, data, callback) {
	return jQuery.ajax({
		headers : {
			'Accept' : 'application/json',
			'Content-Type' : 'application/json'
		},
		'type' : 'POST',
		'async' : false,
		'url' : url,
		'data' : JSON.stringify(data),
		'dataType' : 'json',
		'success' : callback
	});
};