define([ 'underscore', 'backbone', 'translationManager' ], function(_, Backbone, TranslationManager) {

	var Settings = Backbone.Model.extend({
		url : '/rest/amp/settings',
		initialize : function() {
			var _self = this;
			this.fetch({
				async : false,
				error : function(collection, response) {
					console.error('error loading settings');
				},
				success : function(collection, response) {
					_self.teamId = collection.get('team-id');
					_self.teamLead = collection.get('team-lead');
					_self.validator = collection.get('team-validator');
					_self.crossTeamEnable = collection.get('cross_team_validation');
					_self.accessType = collection.get('workspace-type');
					_self.workspacePrefix = collection.get('workspace-prefix');
				}
			});
		}
	});

	return Settings;
});