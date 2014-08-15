/*global Backbone */
var app = app || {};

(function() {
	'use strict';

	// Tab Model
	app.Tab = Backbone.Model.extend({
		defaults : {
			name : '',
			title : '',
			order : 0,
			content : ''
		}
	});

})();
