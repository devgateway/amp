define([ 'underscore', 'backbone', 'models/tab' ], function(_, Backbone, Tab) {
	var Tabs = Backbone.Collection.extend({
		model : Tab,
		url : '/tabs.json',
		initialize : function() {
			console.log('Initialized Tabs Collection');
			this.fetch({
				async : false,
				error : function(collection, response) {
					console.log('error loading tabs.json')
				},
				success : function(collection, response) {
					console.log(response.toString());
				}
			});
		}
	});

	return Tabs;
});
