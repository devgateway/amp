var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/settings.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  events: {
    'click .dash-settings-button': 'editSettings'
  },

  initialize: function(options) {
    this.app = options.app;
    // TODO: hook into state
  },

  render: function() {
    this.$el.html(template({ details: {} }));
    return this;
  },

  editSettings: function() {
    console.log('edit yo...');

    this.$('.dash-settings-modal').modal();
  }

});
