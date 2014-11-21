var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');

// Note: doesn't have an explicit model.
// We can add one for clarity, but not needed.
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({
  url: '/rest/amp/settings',

  serialize: function() {
    var tmpJSON = {};
    this.each(function(setting) {
      if (setting.get('options')) {

        // if nothing selected yet, just take defaultId
        if (!setting.get('selected')) {
          setting.set('selected', setting.get('defaultId'));
        }

        // find the match.
        var match = _.find(setting.get('options'), function(option) {
          /* jshint ignore:start */
          return option.id == setting.get('selected');
          /* jshint ignore:end */
        });
        if (match) {
          tmpJSON[setting.id] = match.id;
        } else {
          console.warn('no match', setting.get('options'), {id: setting.get('selected')});
        }
      }
    });
    return tmpJSON;
  },


  deserialize: function(jsonBlob) {
    var self = this;
    if (jsonBlob) {
      _.each(jsonBlob, function(v, k) {
        self.get(k).set('selected', v);
      });
    } else {
      this.each(function(setting) {
        setting.set('selected', setting.get('defaultId'));
      });
    }
  }

});
