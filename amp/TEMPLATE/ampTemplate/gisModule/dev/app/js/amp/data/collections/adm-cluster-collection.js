var _ = require('underscore');
var Backbone = require('backbone');
var RadioMixin = require('../../mixins/radio-mixin');
var ADMClusterModel = require('../models/adm-cluster-model');

var APIHelper = require('../../../libs/local/api-helper');


module.exports = Backbone.Collection.extend({

  model:  ADMClusterModel,
  url: APIHelper.getAPIBase() + '/rest/gis/cluster',

  initialize: function() {
  },

  // parse geoJson response to pull out features as a collection.
  parse: function (response) {
    return response.features;
  }

});
