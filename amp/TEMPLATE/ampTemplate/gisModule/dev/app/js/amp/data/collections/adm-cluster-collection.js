var Backbone = require('backbone');
var _ = require('underscore');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var ADMClusterModel = require('../models/adm-cluster-model');

/* Backbone Collection ClusterPointsByAdmin aka cluster */
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({

  model:  ADMClusterModel,
  url: '/rest/gis/clusters',

  initialize: function(models, options) {
    // TODO: probably pass app or data here instead?
    this.boundaries = options.boundaries;
    this.filter = options.filter;
    this.settingsWidget = options.settingsWidget;
    this.performanceToggleModel = options.performanceToggleModel;
    this.wocat = false;

    this.listenTo(this, 'sync', this._setDefault);
  },

      reloadWithWocat: function(wocatValue) {
        this.wocat = wocatValue;
        this.model = function(attrs, options) {
          console.log("Wocat reload", wocatValue);
          return new ADMClusterModel(attrs, _.extend({}, options, { wocat: wocatValue }));
        };

        // Update existing models
        this.each(function(model) {
          model.set('wocat', wocatValue);
        });
        this.load();
      },

  parse: function(data) {
    data = _.sortBy(data, function(regionLevel) {
      return regionLevel.id ;
    });
    _.each(data, function(regionLevel) {
      regionLevel.id = regionLevel.adminLevel;
      regionLevel.value = regionLevel.adminLevel;
      regionLevel.title = regionLevel.title;
    });
    return data;
  },

  getSelected: function() {
    return this.chain()
      .filter(function(model) { return model.get('selected'); });
  },

  attachListeners: function() {
    this.each(function(clusterLevel) {
      clusterLevel.attachListeners();
    });
  }

});
