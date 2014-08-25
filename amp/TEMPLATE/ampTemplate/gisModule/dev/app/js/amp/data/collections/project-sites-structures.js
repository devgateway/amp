var Backbone = require('backbone');
var ProjectSitesModel = require('../models/structure-model');


module.exports = Backbone.Collection.extend({

  url: '/rest/gis/structures',

  model: ProjectSitesModel

});
