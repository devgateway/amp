define([ 'underscore', 'backbone', 'models/tab' ], function(_, Backbone, Tab) {
	var Tabs = Backbone.Collection.extend({
		model : Tab
	});
	return Tabs;
});
