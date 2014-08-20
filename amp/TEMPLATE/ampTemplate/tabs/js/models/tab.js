define([ 'underscore', 'backbone' ], function(_, Backbone) {

	// Tab Model
	var Tab = Backbone.Model.extend({
		defaults : {
			name : '',
			id : 0,
			content : ''
		}
	});
	return Tab;
});