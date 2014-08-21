/*https://stackoverflow.com/questions/6535948/nested-models-in-backbone-js-how-to-approach?rq=1*/
define([ 'underscore', 'backbone', '/filter' ], function(_, Backbone, Filter) {

	var TabContent = Backbone.Model.extend({
		model : {
			filter : Filter
		},

		parse : function(response) {
			for ( var key in this.model) {
				var embeddedClass = this.model[key];
				var embeddedData = response[key];
				response[key] = new embeddedClass(embeddedData, {
					parse : true
				});
			}
			return response;
		}
	});

	return TabContent;
});