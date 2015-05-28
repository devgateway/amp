define([ 'underscore', 'backbone', 'business/translations/translationManager' ], function(_, Backbone, TranslationManager) {

	var Settings = Backbone.Model.extend({
		url : '/rest/amp/settings',
		defaults : {
			selectedCurrency : null,
			selectedCalendar : null
		},
		initialize : function() {
			var _self = this;
			this.fetch({
				async : false,
				error : function(collection, response) {
					console.error('error loading settings');
				},
				success : function(collection, response) {
					_self.attributes.currencies = _.find(collection.attributes, function(item) {return item.id === '1'}).options; //SettingsConstants.CURRENCY_ID
					_self.attributes.calendars = _.find(collection.attributes, function(item) {return item.id === '2'}).options; //SettingsConstants.CALENDAR_TYPE_ID 
					_self.teamId = _.find(collection.attributes, function(item) {return item.id === 'team-id'});
					_self.teamLead = _.find(collection.attributes, function(item) {return item.id === 'team-lead'});
					_self.validator = _.find(collection.attributes, function(item) {return item.id === 'team-validator'});
					_self.crossTeamEnable = _.find(collection.attributes, function(item) {return item.id === 'cross_team_validation'});
					// workspace_type
					_self.accessType = _.find(collection.attributes, function(item) {return item.id === 'workspace_type'});
					
					var numberMultiplier = _.find(collection.attributes, function(item) {return item.id === 'number-multiplier'});
					if (numberMultiplier.name === '1.0') {
						_self.numberMultiplierDescription = TranslationManager.getTranslated('Amounts in units');
					} else if(numberMultiplier.name === '0.001') {
						_self.numberMultiplierDescription = TranslationManager.getTranslated('Amounts in thousands');
					} else {
						_self.numberMultiplierDescription = TranslationManager.getTranslated('Amounts in millions');
					}
				}
			});
		}
	});

	return Settings;
});