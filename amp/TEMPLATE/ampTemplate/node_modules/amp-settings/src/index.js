var fs = require('fs');
var jQuery = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
//loading hacks
require('jquery-ui/draggable');
var Config = require('./common/config');
var Constants = require('./common/constants');
var SettingsDefinitionsCollection = require('./collections/settings-definitions-collection');
var SettingsView = require('./views/settings-view');
var GeneralSettings = require('./models/general-settings');
//jquery is colliding in GIS module, so we only load it if it is not loaded or if there is a jquery loaded but version is older than 2.x.x
if (window.$ == undefined || $.fn.jquery.split(' ')[0].split('.')[0] < 2) {
	window.jQuery = window.$ = Backbone.$ = jQuery;
	$.noConflict(true);
}

if (typeof Backbone.$ === 'undefined') {
	console.log('relink backbone');
	Backbone.$ = window.$
}

var bootstrap_enabled = (typeof $().modal == 'function');
if (bootstrap_enabled) {
	require('bootstrap/dist/js/bootstrap');
}


function Widget() {
	this.initialize.apply(this, arguments);
}

_.extend(Widget.prototype, Backbone.Events, {
	Constants: Constants, //expose constants

	initialize : function(options) {
		if (_.isUndefined(options) || _.isUndefined(options.definitionUrl) || _.isUndefined(options.caller)) {
		    throw new Error('definitionUrl and caller are required for the Settings Widget to function correctly.');
		}

		options = _.defaults(options, {
			draggable : true
		});
		options = _.defaults(options, {
			isPopup : Config.IS_POPUP
		});
		options.app = this;
		this.definitions = new SettingsDefinitionsCollection([], options);
		options.definitions = this.definitions;
		this.view = new SettingsView(options);
		this.listenTo(this.view, 'all', function() {
			this.trigger.apply(this, arguments);
		});
		this.definitions.load();
		_.bindAll(this, 'show', 'toAPIFormat', 'restoreFromSaved', 'setElement');
	},
	show : function() {
		this.view.render();
	},
	toAPIFormat : function() {
		return this.view.getCurrent();
	},
	restoreFromSaved : function(state) {
		return this.view.restoreFromSaved(state);
	},
	setElement : function(arguments) {
		this.view.setElement(arguments);
	}
});
module.exports = {SettingsWidget: Widget, GeneralSettings: GeneralSettings}
window.AMPSettings = {SettingsWidget: Widget, GeneralSettings: GeneralSettings};
