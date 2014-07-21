var _ = require('underscore');
var Backbone = require('backbone');
var ADMClusterModel = require('../models/adm-cluster-model');

var APIBase = require('../../../libs/local/api-base');

module.exports = Backbone.Collection.extend({
  model:  ADMClusterModel,
  url: APIBase.getAPIBase() + '/rest/gis/cluster',

  initialize: function() {

  },

  // parse geoJson response to pull out features as a collection.
  parse: function (response) {
    return response.features;
  },

});
