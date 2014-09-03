/*
 * This collection is implemented to be consistent with other layer types
 * (indicator, clusters, etc.), but it really only ever has one model, since
 * project sites are just "one thing" but can be filtered to get alternate
 * views.
 */


var Backbone = require('backbone');
var ProjectSitesModel = require('../models/structures-project-sites-shell-collection-model');


module.exports = Backbone.Collection.extend({

  model: ProjectSitesModel

});
