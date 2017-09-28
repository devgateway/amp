/*https://stackoverflow.com/questions/6535948/nested-models-in-backbone-js-how-to-approach?rq=1*/
/*https://github.com/icereval/backbone-documentmodel*/
define([ 'underscore', 'backbone', 'documentModel' ], function(_, Backbone, DocumentModel) {

	var Content = Backbone.DocumentModel.extend({
		urlRoot : '/rest/data/report/',
		initialize : function() {
			this.fetch({
				async : false,
				error : function(collection, response) {					
				},
				success : function(collection, response) {	
					
				}
			});
		},
		parse : function(resp, xhr) {
			 var data =(resp.data) ? resp.data: resp;			 
			 this.rawFilters = {filters: data.reportMetadata.reportSpec.filters || {} };  	
			 return data;
		},		
		defaults : {
			reportMetadata : {
				name : '',
				reportSpec : {
					filters : {}
				},
				settings : {}
			}
		},
		filtersToJSON : function() {
			  var json = _.clone(this.get('reportMetadata').get('reportSpec').get('filters').attributes);
			  for(var attr in json) {
			    if((json[attr] instanceof Backbone.Model) || (json[attr] instanceof Backbone.Collection)) {
			    	if (json[attr] instanceof Backbone.Collection){
			    		var arr = [];
			    		json[attr].each(function(m) {
			    			arr.push(m.toJSON());
			    	    });
			    		json[attr] = arr;
			    	} else {
			    		json[attr] = json[attr].toJSON();
			    	}			               
			    }
			  }
			  return {filters:json};
		}
	});

	return Content;
});