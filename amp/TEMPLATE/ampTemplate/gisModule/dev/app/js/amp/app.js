// set up jquery, bootstrap, etc.
require('../libs/local/lib-load-hacks');

var $ = require('jquery');

var GISData = require('./data/gis-data');
var App = require('./gis/views/gis-main');

var State = require('amp-state/index'); //require('./services/state');
var translator = require('./services/translator');
var WindowTitle = require('./services/title');
var URLService = require('./services/url');
var data = new GISData();
var url = new URLService();
var state = new State({ url: url, saved: data.savedMaps, autoinit: true, prefix: ['saved/', 'report/']});


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

function configureApp(){
	data.initializeCollectionsAndModels();	
	data.addState(state);	
	app.translator = translator.init();
	app.createViews();
	app.data.load();
	// initialize everything that doesn't need to touch the DOM
	$(document).ready(function() {
		// Attach to the DOM and do all the dom-y stuff
		app.setElement($('#gis-plugin')).render();
	});	
	// hook up the title
	var windowTitle = new WindowTitle('Aid Management Platform - GIS');
	//windowTitle.listenTo(app.data.title, 'update', windowTitle.set);
}

module.exports = window.app = app;