var Backbone = require('backbone');
var ProjectSitesModel = require('../models/project-sites-model');


module.exports = Backbone.Collection.extend({

  // LEGACY
  url: '/rest/gis/cluster',

  initialize : function() {
    this.add([  // TODO: move to gis-data
      new ProjectSitesModel()
    ]);

  }

});
