define([ 'underscore', 'backbone' ], function(_, Backbone) {

	// Tab Model
	var Tab = Backbone.Model.extend({
		defaults : {
			name : '',
			title : '',
			order : 0,
			content : ''
		}
	});
	return Tab;
});