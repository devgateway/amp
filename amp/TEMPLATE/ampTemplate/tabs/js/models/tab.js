define([ 'underscore', 'backbone' ], function(_, Backbone) {

	// Tab Model
	var Tab = Backbone.Model.extend({
		defaults : {
			name : '',
			shortName: '',
			id : 0,
			/* it means is one of the tabs always shown. */
			visible : false,
			/* it means is originally from the 'more tabs' and is visible now. */
			isOtherTabNowVisible : false
		}
	});
	return Tab;
});