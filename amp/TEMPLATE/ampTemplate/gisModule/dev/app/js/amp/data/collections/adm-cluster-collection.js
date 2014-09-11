var Backbone = require('backbone');
var ADMClusterModel = require('../models/adm-cluster-model');


module.exports = Backbone.Collection.extend({

  model:  ADMClusterModel,
  url: '/rest/gis/cluster',

  initialize: function(models, options) {
    // TODO: probably pass app or data here instead?
    this.boundaries = options.boundaries;
  },

  // parse geoJson response to pull out features as a collection.
  parse: function (response) {
    return response.features;
  }

});
