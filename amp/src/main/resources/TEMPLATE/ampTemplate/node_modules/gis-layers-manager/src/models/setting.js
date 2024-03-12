var _ = require('underscore');
var Backbone = require('backbone');
module.exports = Backbone.Model.extend({
	url: '/rest/amp/settings'
});