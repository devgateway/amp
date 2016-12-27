/*https://stackoverflow.com/questions/6535948/nested-models-in-backbone-js-how-to-approach?rq=1*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'underscore', 'backbone', 'documentModel' ], function(_, Backbone, DocumentModel) {

	var Content = Backbone.DocumentModel.extend({
		urlRoot : '/rest/data/report/',
		initialize : function() {
			this.fetch({
				async : false,
				error : function(collection, response) {
					console.log(collection);
				},
				success : function(collection, response) {
					console.log(collection);
				}
			});
		},
		parse : function(resp, xhr) {
			 var data =(resp.data) ? resp.data: resp;			 
			 this.rawFilters = {filters: this.processFilters(data.reportMetadata.reportSpec.filters || {}) };  	
			 return data;
		},
		processFilters: function(filters){
			var processedFilters = {}
			for ( var propertyName in filters) {
				var filter = filters[propertyName];
				if(Array.isArray(filter)){
					var values = [];
					_.each(filter, function(item) {
						var value = isNaN(item) ? item : parseInt(item);
						values.push(value);
					});
					processedFilters[propertyName] = values; 
				} else {					
					processedFilters[propertyName] = isNaN(filter) ? filter : parseInt(filter);					
				}
			}
			return processedFilters;
			
		},
		defaults : {
			reportMetadata : {
				name : '',
				reportSpec : {
					filters : {}
				},
				settings : {}
			}
		}
	});

	return Content;
});