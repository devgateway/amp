/* This model represents a single filter that can be applied on a report/tab. */
define([ 'underscore', 'backbone' ], function(_, Backbone) {

	var Filter = Backbone.Model.extend({
		defaults : {
			name : '',
			value : ''
		}
	});
  
	return Filter;
});