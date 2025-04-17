var Backbone = require('backbone');
var _ = require('underscore');

var HeatmapsConfigModel = Backbone.Model.extend({

});

var HeatmapsConfigCollection = Backbone.Collection.extend({
	model : HeatmapsConfigModel,
	url : '/rest/dashboard/heat-map/configs',
	fetchData : function() {
		this.fetch({
			type : 'GET',
			async : false,
			processData : false,
			mimeType : 'application/json',
			traditional : true,
			headers : {
				'Content-Type' : 'application/json',
				'Cache-Control' : 'no-cache'
			},
			data : JSON.stringify(), // This is necessary due to
											// incompatibilities with Jersey
											// when receiving the params.
			error : function(collection, response) {
				console.error('error loading heatmap configs.');
			},
			success : function(collection, response) {
				//console.log(response);
			}
		});
	}
});

module.exports = HeatmapsConfigCollection;