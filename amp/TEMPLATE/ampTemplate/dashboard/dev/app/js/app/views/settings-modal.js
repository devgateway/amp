var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var AmpLogger = require('../../../../../../../ampTemplate/module/amp-log')('settings:modal');
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
    //if the browser supports local storage AND (the current setting is <currency> OR it is <calendar>)
    if(!this.app.hasIssue('localStorage') && _(['currency', 'Calendar Type']).contains(this.current.attributes.name)){
      var settings;
      //try reading the settings JSON from localStorage and deserialize it...
      try{
        settings = JSON.parse(localStorage.settings);
      }
      catch(e){}
      //...regardless of whether that failed or not...
      finally{
        //...ensure settings is a normal object...
        if(!("object" == typeof settings && null !== settings)){
          //...if it's not, let it be just an empty object
          settings = {};
        }
      }
      //and after all that paranoia we can now relatively safely persist the settings:
      settings[this.current.attributes.name] = optionId;
      localStorage.settings = JSON.stringify(settings);
    }
    this.current.select(optionId);
  }
});
