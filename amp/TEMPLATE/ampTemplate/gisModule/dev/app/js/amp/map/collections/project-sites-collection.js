var _ = require('underscore');
var Backbone = require('backbone');
var ProjectSiteModel = require('../models/project-site-model');

var APIHelper = require('../../../libs/local/api-helper');

module.exports = Backbone.Collection.extend({
  model:  ProjectSiteModel,
  url: APIHelper.getAPIBase() + '/rest/gis/project-sites',

  initialize: function() {

  },

  // parse geoJson response to pull out features as a collection.
  parse: function (response) {
    return response.features;
  },

});
