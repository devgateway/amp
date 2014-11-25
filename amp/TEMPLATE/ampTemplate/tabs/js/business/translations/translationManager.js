define([ 'underscore', 'jquery', 'i18next' ], function(_, jQuery) {

	"use strict";

	var prefix = 'tabs.common:';

	function TranslationManager() {
		if (!(this instanceof TranslationManager)) {
			throw new TypeError("TranslationManager constructor cannot be called as a function.");
		}
	}

	TranslationManager.prototype = {
		constructor : TranslationManager
	};

	TranslationManager.searchAndTranslate = function() {
		initializeGlobalTranslationsCache();

		var postit = {};
		_.each($("*[data-i18n^='tabs.']"), function(i, val) {
			var key = $(i).attr('data-i18n');
			var value = $(i).text();
			var translationFromCache = lookForTranslationByKey(key);
			if (translationFromCache == null) {
				postit[key] = value;
			}
		});
		// Do not call endpoint when there is nothing to translate to save some
		// extra ms (500ms in Timor!).
		if (!jQuery.isEmptyObject(postit)) {
			postJSON('/rest/translations/label-translations', postit, function(data) {
				$.each(data, function(key, value) {
					putTranslationValueInCache(key, value);
				});
				$.each(app.TabsApp.globalTranslationCache, function(key, value) {
					$("*[data-i18n='" + key + "']").text(value);
				});
			});
		} else {
			$.each(app.TabsApp.globalTranslationCache, function(key, value) {
				$("*[data-i18n='" + key + "']").text(value);
			});
		}
	};

	TranslationManager.getTranslated = function(text) {
		initializeGlobalTranslationsCache();
		var translationFromCache = lookForTranslationByKey(prefix + text);
		if (translationFromCache != null) {
			return translationFromCache;
		} else {
			var textObject = {};
			var key = prefix + text;
			textObject[key] = text;
			var response = (postJSON('/rest/translations/label-translations', textObject, function(data) {
				console.log(data);
			}));
			putTranslationValueInCache(key, response.responseJSON[key]);
			return response.responseJSON[key];
		}
	};

	function postJSON(url, data, callback) {
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
	}

	function initializeGlobalTranslationsCache() {
		if (app.TabsApp.globalTranslationCache == undefined || app.TabsApp.globalTranslationCache == null) {
			app.TabsApp.globalTranslationCache = {};
		}
	}

	function putTranslationValueInCache(key, value) {
		app.TabsApp.globalTranslationCache[key] = value;
	}

	function lookForTranslationByKey(key) {
		var translation = app.TabsApp.globalTranslationCache[key];
		if (translation) {
			// console.log('found translation: ' + key);
			return translation;
		} else {
			return null;
		}
	}

	return TranslationManager;
});