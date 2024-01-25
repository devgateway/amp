// set up jquery, bootstrap, etc.
require('../libs/local/lib-load-hacks');

var $ = require('jquery');

require('@babel/polyfill');
var GISData = require('./data/gis-data');
var App = require('./gis/views/gis-main');

var $ = require('jquery');
var State = require('amp-state/index'); //require('./services/state');
var translator = require('./services/translator');
var WindowTitle = require('./services/title');
var URLService = require('./services/url');
var Constants = require('./data/constants');
var data = new GISData();
var url = new URLService();
var state = new State({ url: url, saved: data.savedMaps, autoinit: true, prefix: ['saved/', 'report/']});
var constants = new Constants();

var app = new App({
	  url: url,
	  data: data,
	  state: state
});



//if saved data is loading, wait till its ready
if(state.loadPromise){
	state.loadPromise.always(function(){
		setSavedLanguage().then(function(){
			configureApp();
		});
	});	
} else {
	configureApp();
}

let cachedGisSettings = null;

function getGisSettings() {
	return new Promise((resolve, reject) => {
		if (cachedGisSettings !== null) {
			resolve(cachedGisSettings);
			return;
		}

		fetch('/rest/amp/settings/gis')
			.then(response => {
				if (!response.ok) {
					throw new Error('Network response was not ok');
				}
				return response.json();
			})
			.then(data => {
				// Cache the GIS settings
				cachedGisSettings = data;

				// Resolve the promise with the JSON response
				resolve(data);
			})
			.catch(error => {
				// Reject the promise with the error
				reject(error);
			});
	});
}

//configure to use saved language
function setSavedLanguage(){
	var deferred = $.Deferred();
	var lang = getLanguageFromState();
	if(lang){
		$.get( '/rest/translations/languages/' + lang, function() {}).always(function(){
			deferred.resolve();
		});		
	}else{
		deferred.resolve();
	}
	return deferred;	
}

//get language in saved map
function getLanguageFromState(){
	var lang;
	if(state.saved.models.length > 0){
		var stateBlob = state.parse(state.saved.models[0].get('stateBlob'));
		if(stateBlob && stateBlob.settings && stateBlob.settings.language){
			lang = stateBlob.settings.language
		}
	}
	return lang;	
}



function configureApp() {
	data.initializeCollectionsAndModels();
	data.addState(state);

	// Ensure proper chaining by returning the promise from getGisSettings
			// The code inside this block will be executed after getGisSettings is resolved or rejected

			app.translator = translator.init();
			app.constants = constants;
			app.createViews();
			app.data.load();

			// initialize everything that doesn't need to touch the DOM
			$(document).ready(function () {
				// Attach to the DOM and do all the dom-y stuff
				app.setElement($('#gis-plugin')).render();
			});

			// hook up the title
			var windowTitle = new WindowTitle('Aid Management Platform - GIS');
			// windowTitle.listenTo(app.data.title, 'update', windowTitle.set);

}



module.exports = {
	getGisSettings,
};

module.exports = window.app = app;