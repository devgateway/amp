var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var Backbone = require('backbone');
module.exports = Backbone.Model.extend({
	url: '/rest/amp/settings',	
	firstTime: true,
	initialize: function() {
		this.loaded = new Deferred();
		_.bindAll(this,'load');
	},
	parse: function(settings){
	  return settings;
	},	
	load: function() {
		if (this.firstTime) {
			this.firstTime = false;
			if (this.loaded.state() !== 'pending') { return this.loaded.promise(); }
			this.fetch({})
			.then(_(function() {
				this.loaded.resolve();
			}).bind(this))
			.fail(_(function() {			       
				this.loaded.reject();
			}).bind(this));
		}    
		return this.loaded.promise();
	}
});