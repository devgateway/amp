define([ 'underscore', 'backbone', 'models/filter' ], function(_, Backbone, Filter) {
	var Filters = Backbone.Collection.extend({
		model : Filter,
		comparator : function(m) {
			return m.get('name');
		}
	});

	return Filters;
});
