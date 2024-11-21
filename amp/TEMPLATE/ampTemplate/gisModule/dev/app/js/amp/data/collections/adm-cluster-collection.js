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
    console.log("Options in collection", options);
    // TODO: probably pass app or data here instead?
    this.boundaries = options.boundaries;
    this.filter = options.filter;
    this.settingsWidget = options.settingsWidget;
    this.performanceToggleModel = options.performanceToggleModel;
    this.wocat = options.wocat;
    this.model = function(attrs, options) {
      return new ADMClusterModel(attrs, _.extend({}, options, { wocat: this.wocat }));
    };
    this.listenTo(this, 'sync', this._setDefault);
  },

      reloadWithWocat: function() {
        // Ensure `wocatValue` is boolean

        console.log("Reloading with wocat:", this.wocat);

        // Reset the collection if necessary (clears all models)
        this.reset();

        // Fetch the collection data with `wocat` as a parameter
        this.fetch({
          success: (collection) => {
            console.log("Fetch successful. Updating models with wocat:", this.wocat);

            // Update each model with the `wocat` property
            collection.each((model) => {
              model.set('wocat', this.wocat);
            });

            console.log("All models updated successfully.");
          },
          error: (collection, response) => {
            console.error("Error fetching collection:", response);
          }
        });
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
