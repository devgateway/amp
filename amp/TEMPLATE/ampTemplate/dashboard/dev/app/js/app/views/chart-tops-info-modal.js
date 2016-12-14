var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(__dirname
		+ '/../templates/chart-tops-info-modal.html', 'UTF-8'));

module.exports = BackboneDash.View.extend({

	initialize: function(options) {
		this.app = options.app;
		this.context = options.context;
		this.model = options.model;
		this.numberDivider = app.generalSettings.numberDivider;			
	},

	render: function() {
		var self = this;
		this.$el.html(template({
			error: undefined,
			model: this.model,
			context: this.context,
			values: undefined,
			numberDivider: this.numberDivider
		}));
		app.translator.translateDOM($(".dash-settings-modal"));
		
		//TODO: move this code to a new model so the API call is made automatically.
    	var config = this.app.filter.serialize();
    	config.settings = this.app.settingsWidget.toAPIFormat();
    	config.settings['funding-type'] = this.model.get('adjtype');
    	$.ajax({
    		method: 'POST',
    		url: self.model.url + '/' + this.context.data[0].values[this.context.x.index].id,
    		dataType: 'json',
    		contentType: 'application/json',
    		processData: false,
    		data: JSON.stringify(config)
    	}).done(function(data) {
    		//TODO: Can we avoid re-calling the template by binding the changes in the 'values' field? 
    		self.$el.html(template({
				error: undefined,
    			model: self.model,
    			context: self.context,
    			values: data.values,
    			numberDivider: self.numberDivider
    		}));
    		app.translator.translateDOM($(".dash-settings-modal"));
    	}).fail(function(xhr, err) {
			var msg = JSON.parse(xhr.responseText).error;
			console.error("Error Getting chart-tops-info-modal from EP", msg);
			self.$el.html(template({
				model: self.model,
				context: self.context,
				error: err,
				numberDivider: self.numberDivider
			}));
		});
    	
		return this;
	},

});