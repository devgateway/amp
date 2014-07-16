var _ = require('underscore');
var Backbone = require('backbone');
var ProjectSiteModel = require('../models/project-site-model');


module.exports = Backbone.Collection.extend({
  model:  ProjectSiteModel,
  url: '/js/mock-api/project-sites.json',

  initialize: function() {
    
  },

  // parse geoJson response to pull out features as a collection.
  parse: function (response) {
    return response.features;
  },

});
