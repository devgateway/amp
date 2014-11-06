define([ 'underscore', 'backbone' ], function(_, Backbone) {

	var Settings = Backbone.Model.extend({
		url : '/rest/gis/settings',
		defaults : {
			selectedCurrency : null,
			selectedCalendar : null
		},
		initialize : function() {
			var _self = this;
			console.log('Initialized Settings');
			this.fetch({
				async : false,
				error : function(collection, response) {
					console.error('error loading settings');
				},
				success : function(collection, response) {
					console.log(response);
					_self.attributes.currencies = collection.get('0').options;
					_self.attributes.calendars = collection.get('1').options;
				}
			});
		}
	});

	return Settings;
});