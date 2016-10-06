var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var logger = require('../../../../../../../reamp/tools/log')('amp:dashboards:settings:modal');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/settings-modal.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  events: {
    'click .setting-select': 'selectSetting',
    'change .setting-value': 'changeSetting',
    'click .apply': 'applySettings'
  },

  initialize: function(options) {
    this.app = options.app;
    logger.log("Initialized with", options);
  },

  render: function() {
    if (!this.current) {
      this.current = this.app.settings.getVisible()[0];
      logger.log("Render requested, but there's no  current setting. Trying to guess it");
    }
    this.$el.html(template({
      settings: this.app.settings,
      current: this.current
    }));
    var that = this;
    logger.onDebug(function(){
      logger.log("Rendered width current=", that.current.toJSON());
    });
    return this;
  },

  selectSetting: function(e) {
    e.preventDefault();  // don't change URL
    var settingId = e.currentTarget.hash.slice(1);  // removes '#'
    this.current = this.app.settings.get(settingId);
    var that = this;
    logger.onDebug(function(){
      logger.log("Current setting changed to", that.current.toJSON());
    });
    this.render();
  },

  changeSetting: function(e) {
    var optionId = e.currentTarget.value;
    logger.log("Changing", optionId, "setting"); 
    this.current.select(optionId, false);
  },
  
  applySettings: function(e) {
	  this.current.apply();
  }
});
