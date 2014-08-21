define([ 'underscore', 'backbone', 'models/filter' ], function(_, Backbone, Filter) {

	var Filters = Backbone.Collection.extend({
		model : Filter,
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

	return Filters;
  
});