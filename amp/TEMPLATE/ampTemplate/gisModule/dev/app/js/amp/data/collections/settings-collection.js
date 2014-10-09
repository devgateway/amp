var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');


module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({
  url: '/TEMPLATE/ampTemplate/gisModule/dev/app/mock-api/data/settings.json',

  serialize: function() {
    var tmpJSON = {};
    this.each(function(setting) {
      tmpJSON[setting.id] = _.findWhere(setting.get('options'), {id: setting.get('selected')});
    });
    return tmpJSON;
  },

  deserialize: function(jsonBlob) {
    // TODO
    console.log('jsonBlob', jsonBlob);
  }

});
