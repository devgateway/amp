define([ 'underscore', 'backbone' ], function(_, Backbone) {

	var Legend = Backbone.Model.extend({
		defaults : {
			id : null,
			currencyCode : null,
			units : null
		}
	});
	return Legend;
});