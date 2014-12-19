var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var Setting = require('./setting');


module.exports = BackboneDash.Collection.extend({

  url: '/rest/amp/settings',

  model: Setting,

  comparator: 'id',

  initialize: function(models, options) {
    this.app = options.app;
    this._loaded = new Deferred();
    _.bindAll(this, 'toAPI', 'fromAPI');
  },

  parse: function(settings) {
    var goodSettings = _(settings).filter(function(setting) {
      return !isNaN(parseInt(setting.id, 10)) &&  // some settings are weird... take em out for now
      setting.id !== '0';  // leave funding type to the charts
    }).map(function(setting) {  // mark defaults as selected
      return _(setting).extend({
        options: _(setting.options).map(function(option) {
          if (option.id === setting.defaultId) {
            return _(option).extend({ selected: true });
          } else {
            return option;
          }
        })
      });
    });
    return goodSettings;
  },

  load: function() {
    if (this._loaded.state() !== 'pending') { return this._loaded.promise(); }

    this.fetch({app: this.app })
      .then(_(function() {
        this._loaded.resolve();
      }).bind(this))
      .fail(_(function() {
        this.app.report('Failed to load dashboard settings',
          ['Could not connect to the server.']);
        this._loaded.reject();
      }).bind(this));

    return this._loaded.promise();
  },

  toAPI: function() {
    return this.reduce(function(payload, setting) {
      payload[setting.id] = _(setting.get('options'))
        .findWhere({selected: true}).id;
      return payload;
    }, {});
  },

  fromAPI: function(state) {
    _(state).each(function(optId, settingId) {
      this.get(settingId).select(optId);
    }, this);
  }

});
