var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');

// Note: doesn't have an explicit model.
// We can add one for clarity, but not needed.
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({
  url: '/rest/amp/settings',

  serialize: function() {
    var tmpJSON = {};
    var self = this;
    this.each(function(setting) {
      if (setting.get('options')) {

        // if nothing selected yet, just take defaultId
        if (!setting.get('selected')) {
          setting.set('selected', setting.get('defaultId'));
        }

        /* TODO: If selectedName is getting persisted in share url, consider removing here*/
        self.setSettingSelectedName(setting);

        // find the match.
        var match = _.find(setting.get('options'), function(option) {
          // make sure everything is strings... (compatibility with former == use, maybe remove cast)
          return ('' + option.id) === ('' + setting.get('selected'));
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

  serializeDeferred: function() {
    var self = this;
    var deferred = $.Deferred();

    this.load().then(function() {
      deferred.resolve(self.serialize());
    });
    return deferred;
  },

  /* Make the server-side localized name of the chosen setting value
   *   available as selectedName on setting model

   * e.g. selected is: Planned Disbursements,
   *      so then selectedName is: Debursări planificate
   * e.g. selected is: EUR,
   *      so then selectedName is: Euro
   */
  setSettingSelectedName: function (setting) {
    if (setting && setting.get('options')) {
      var settingObject = _(setting.get('options'))
            .findWhere({
              id: setting.get('selected')
            });

      var translatedSetting = settingObject.name;
      setting.set('selectedName', translatedSetting );
    }
  },

  deserialize: function(jsonBlob) {
    var self = this;
    if (jsonBlob) {
      _.each(jsonBlob, function(v, k) {
        self.get(k).set('selected', v);

        /* also stash translated item's name */
        var setting = self.get(k);
        self.setSettingSelectedName(setting);

      });
    } else {
      this.each(function(setting) {
        setting.set('selected', setting.get('defaultId'));
        self.setSettingSelectedName(setting);
      });

    }
  }

});
