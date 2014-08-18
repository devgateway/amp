var _ = require('underscore');
var Backbone = require('backbone');
var RadioMixin = require('../../mixins/radio-mixin');
var ADMClusterModel = require('../models/adm-cluster-model');

var APIHelper = require('../../../libs/local/api-helper');


module.exports = Backbone.Collection.extend({

  model:  ADMClusterModel,
  url: APIHelper.getAPIBase() + '/rest/gis/cluster',

  initialize: function(models, options) {
    // TODO: probably pass app or data here instead?
    this.boundaries = options.boundaries;
  },

  // parse geoJson response to pull out features as a collection.
  parse: function (response) {
    return response.features;
  }

});
