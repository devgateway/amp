define([ 'underscore', 'jquery', 'i18next' ], function(_, jQuery) {

	"use strict";

	function TranslationManager() {
		if (!(this instanceof TranslationManager)) {
			throw new TypeError("TranslationManager constructor cannot be called as a function.");
		}
	}

	TranslationManager.prototype = {
		constructor : TranslationManager
	};

	TranslationManager.searchAndTranslate = function() {
		var postit = {};
		_.each($("*[data-i18n^='tabs.']"), function(i, val) {
			var key = $(i).attr('data-i18n');
			var value = $(i).text();
			postit[key] = value;
		});
		postJSON('/rest/translations/label-translations', postit, function(data) {
			$.each(data, function(key, value) {
				$("*[data-i18n='" + key + "']").text(value);
			});
		});
	};

	function postJSON(url, data, callback) {
		return jQuery.ajax({
			headers : {
				'Accept' : 'application/json',
				'Content-Type' : 'application/json'
			},
			'type' : 'POST',
			'url' : url,
			'data' : JSON.stringify(data),
			'dataType' : 'json',
			'success' : callback
		});
	}

	return TranslationManager;
});