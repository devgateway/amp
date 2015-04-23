define([ 'underscore', 'backbone' ], function(_, Backbone) {

	var Settings = Backbone.Model.extend({
		url : '/rest/amp/settings',
		defaults : {
			selectedCurrency : null,
			selectedCalendar : null,
			crossteamvalidation : null,
			TeamManager : null,
			TeamValidator : null,
			TeamId : null
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
					_self.attributes.teamid = collection.get('5');
					_self.attributes.teamlead = collection.get('6');
					_self.attributes.validator = collection.get('7');
					_self.attributes.crossteamenable = collection.get('8');
					//workspace type
					_self.attributes.accestype = collection.get('13');
				}
			});
		}
	});

	return Settings;
});