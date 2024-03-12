var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(__dirname + '/../templates/settings.html', 'UTF-8'));
module.exports = BackboneDash.View.extend({
  events: {
    'click .dash-settings-button': 'editSettings'
  },
  initialize: function(options) {
    this.app = options.app;    
    this.app.settingsWidget.definitions.loaded.done(_(function() {
     this.app.state.register(this, 'settings', {
        get: this.app.settingsWidget.toAPIFormat,
        set: this.app.settingsWidget.restoreFromSaved
      });
    }).bind(this));
  },
  render: function() {
    this.$el.html(template()); 
    this.app.settingsWidget.setElement(this.el.querySelector('#amp-settings'));
    return this;
  },
  editSettings: function() {
	var self = this;
	this.app.settingsWidget.show();
	this.$('#amp-settings').show();
	this.app.settingsWidget.on('close', function() {
		self.$('#amp-settings').hide();
	});
	
	this.app.settingsWidget.on('applySettings', function() {
		self.$('#amp-settings').hide();
	});	
 }

});
