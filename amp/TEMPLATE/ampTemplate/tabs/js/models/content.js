/*https://stackoverflow.com/questions/6535948/nested-models-in-backbone-js-how-to-approach?rq=1*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'underscore', 'backbone', 'documentModel' ], function(_, Backbone, DocumentModel) {

	var Content = Backbone.DocumentModel.extend({
		urlRoot : '/rest/data/report/',
		initialize : function() {
			console.log('Initialized Content object');
			this.fetch({
				async : false,
				error : function(collection, response) {
					console.log('error loading content from server');
				},
				success : function(collection, response) {
					console.log(response);
				}
			});
		},
		defaults : {
			reportMetadata : {
				name : '',
				reportSpec : {
					filters : {
						filterRules : {

						}
					}
				},
				settings : []
			}
		}
	});

	return Content;
});