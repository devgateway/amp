var Backbone = require('backbone');
module.exports  = Backbone.Collection.extend({
	url: function() {
		return '/rest/security/workspaces'
	}
});