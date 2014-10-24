var Backbone = require('backbone');
var ADMClusterModel = require('../models/adm-cluster-model');

/* Backbone Collection ClusterPointsByAdmin aka cluster */
module.exports = Backbone.Collection.extend({

  model:  ADMClusterModel,
  url: '/rest/gis/cluster',

  initialize: function(models, options) {
    // TODO: probably pass app or data here instead?
    this.boundaries = options.boundaries;
    this.filter = options.filter;
  },

  // parse geoJson response to pull out features as a collection.
  parse: function(response) {
    return response.features;
  },

  getSelected: function() {
    return this.chain()
      .filter(function(model) { return model.get('selected'); });
  }

});
