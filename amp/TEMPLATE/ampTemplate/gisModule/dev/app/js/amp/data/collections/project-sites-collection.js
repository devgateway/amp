var _ = require('underscore');
var Backbone = require('backbone');
var ProjectSitesModel = require('../models/project-sites-model');

var APIHelper = require('../../../libs/local/api-helper');


//TODO move projectLayerCollection to app.data
module.exports = Backbone.Collection.extend({

  // LEGACY
  url: APIHelper.getAPIBase() + '/rest/gis/cluster',

  initialize : function(videos) {
    this.add([  // TODO: move to gis-data
      new ProjectSitesModel()
    ]);

  }

});
