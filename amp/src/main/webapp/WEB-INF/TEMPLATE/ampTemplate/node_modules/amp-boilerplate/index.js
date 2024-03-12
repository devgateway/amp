var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
//loading hacks
var jQuery = require('jquery');
var jQueryUI = require('jquery-ui')
var dtp = require('jquery-ui/datepicker');

//loading jquery is colliding in GIS module, so we only load it if
//it is not loaded or if there is a jquery loaded but version is older than 2.x.x
if (window.$ == undefined || $.fn.jquery.split(' ')[0].split('.')[0] < 2) {
	window.jQuery = window.$ = Backbone.$ = jQuery;
	Backbone.$ = window.$
	console.log('relink jquery');
}

var bootstrap_enabled = (typeof $().modal == 'function');
if (bootstrap_enabled) {
	require('bootstrap/dist/js/bootstrap');
}

var HeaderView = require('./src/views/header-view.js');
var FooterView = require('./src/views/footer-view.js');
var Translator = require('amp-translate');
var LayoutModel = require('./src/models/amp-layout-model.js');

/* example of use
 * this.menus = new Menus({
 *   translator: this.translator,
 *   caller: 'GIS'
 * });
 */

function Widget() {
	this.initialize.apply(this, arguments);
}

_.extend(Widget.prototype, Backbone.Events, {
	layoutFetched : new $.Deferred(),
	initialize : function(options) {
		options = _.defaults(options, {
			showFooterAdmin : true,
			showDGFooter : true,
			showLogin : true,
			useSingleRowHeader : false
		});
		if (_.has(options, 'sync')) {
			Backbone.sync = options.sync;
		}

		var self = this;
		this.createTranslator();
		this.createViews(options);
		 _.bindAll(this, 'createTranslator', 'createViews','bubbleViewEvents','onMenuRendered');
	},
	createTranslator : function() {
		var defaultKeys = JSON.parse(fs.readFileSync(__dirname + '/src/services/initial-translation-request.json', 'utf8'));
		this.translator = new Translator({
			defaultKeys : defaultKeys
		});
	},
	createViews : function(options) {
		var self = this;
		options.translator = this.translator;
		this.layoutModel = new LayoutModel();
		options.layoutFetched = this.layoutFetched;
		this.layoutModel.fetch().then(function(layout) {
			options.model = layout;
			window.buildDate = layout.buildDate;
			window.ampVersion = layout.ampVersion;
			self.header = new HeaderView(options);
			self.footer = new FooterView(options);
			self.onMenuRendered();
			self.bubbleViewEvents();
			self.layoutFetched.resolve();
		});
	},
	bubbleViewEvents : function() {
		this.listenTo(this.footer, 'all', function() {
			this.trigger.apply(this, arguments);
		});

		this.listenTo(this.header, 'all', function() {
			this.trigger.apply(this, arguments);
		});
	},
	onMenuRendered: function(){
		var self = this;
		$.when(this.header.menuRendered, this.layoutFetched).then(function() {
			self.translator.translateDOM(document);
			if ($.fn.dropdown !== undefined) {
				$('.dropdown-toggle').dropdown();
			}
		});
	}

});

module.exports = {
	layout : Widget
};
window.boilerplate = Widget;
