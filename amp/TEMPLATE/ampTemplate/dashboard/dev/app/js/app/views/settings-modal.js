var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/settings-modal.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  events: {
    'click .setting-select': 'selectSetting',
    'change .setting-value': 'changeSetting'
  },

  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    if (!this.current) { this.current = this.app.settings.getVisible()[0]; }
    this.$el.html(template({
      settings: this.app.settings,
      current: this.current
    }));
    return this;
  },

  selectSetting: function(e) {
    e.preventDefault();  // don't change URL
    var settingId = e.currentTarget.hash.slice(1);  // removes '#'
    this.current = this.app.settings.get(settingId);
    this.render();
  },

  changeSetting: function(e) {
    var optionId = e.currentTarget.value;
    this.current.select(optionId);
  }

});
