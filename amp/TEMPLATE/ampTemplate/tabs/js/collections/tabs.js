/*global Backbone */
var app = app || {};

(function() {
	'use strict';

	app.Tabs = Backbone.Collection.extend({
		// Reference to this collection's model.
		model : app.Tab
	});

})();