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
					
					var foundTeam =_.find(collection.attributes, function(item) {return item.id === 'team-id'});
					_self.teamId = _.find(foundTeam.options, function(item) { return item.id === foundTeam.defaultId}).name;
					
					var foundTeamLead =_.find(collection.attributes, function(item) {return item.id === 'team-lead'});
					_self.teamLead = _.find(foundTeamLead.options, function(item) { return item.id === foundTeamLead.defaultId}).name;
					
					var foundValidator =_.find(collection.attributes, function(item) {return item.id === 'team-validator'});					
					_self.validator = _.find(foundValidator.options, function(item) { return item.id === foundValidator.defaultId}).name;
					
					var foundCross =_.find(collection.attributes, function(item) {return item.id === 'cross_team_validation'});
					_self.crossTeamEnable = _.find(foundCross.options, function(item) { return item.id === foundCross.defaultId}).name;
					
					// workspace_type
					var foundAT =_.find(collection.attributes, function(item) {return item.id === 'workspace_type'});
					_self.accessType = _.find(foundAT.options, function(item) { return item.id === foundAT.defaultId}).name;

					var foundMulti =_.find(collection.attributes, function(item) {return item.id === 'number-multiplier'});
					var numberMultiplier = _.find(foundMulti.options, function(item) { return item.id === foundMulti.defaultId}).name;
					if (numberMultiplier === '1.0') {
						_self.numberMultiplierDescription = TranslationManager.getTranslated('Amounts in units');
					} else if(numberMultiplier === '0.001') {
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