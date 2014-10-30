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

  /*
   * API spec: [
   *  {
   *    "id": 1,
   *    "title": "Country",
   *    "adminLevel": "adm-0"
   *  },
   *  {
   *    "id": 77,
   *    "title": "Region",
   *    "adminLevel": "adm-1"
   *  },
   *  {
   *    "id": 133,
   *    "title": "Zone",
   *    "adminLevel": "adm-2"
   *  }
   *  ...
   *]
   */

  getSelected: function() {
    return this.chain()
      .filter(function(model) { return model.get('selected'); });
  }

});
