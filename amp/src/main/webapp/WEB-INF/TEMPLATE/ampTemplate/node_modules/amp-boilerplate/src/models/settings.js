var _ = require('underscore');
var Backbone = require('backbone');
module.exports = Backbone.Model.extend({
    fetch: function (options) {
        options = options || {};
        options.cache = false;
        return Backbone.Collection.prototype.fetch.call(this, options);
    },
	url: '/rest/amp/settings'
});