/*
 * This collection is implemented to be consistent with other layer types
 * (indicator, clusters, etc.), but it really only ever has one model, since
 * project sites are just "one thing" but can be filtered to get alternate
 * views.
 */


var Backbone = require('backbone');
var ProjectSitesModel = require('../models/project-sites-model');


module.exports = Backbone.Collection.extend({

  // LEGACY
  url: '/rest/gis/cluster',

  model: ProjectSitesModel

});
