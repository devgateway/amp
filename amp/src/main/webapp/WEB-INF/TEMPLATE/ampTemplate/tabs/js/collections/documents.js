define([ 'underscore', 'backbone', 'models/document' ], function(_, Backbone, Document) {
	var Documents = Backbone.Collection.extend({
		model : Document,
		url : '/rest/documents/getTopDocuments',
		fetchData : function() {			
			this.fetch({
				async : false,
				error : function(collection, response) {
					console.error('error loading documents url');
				},
				success : function(collection, response) {					
				}
			});
		}
	});

	return Documents;
});