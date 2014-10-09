var _ = require('underscore');
var Backbone = require('backbone');
var ProjectSitesModel = require('../models/structure-model');
var ActivityCollection = require('../collections/activity-collection');


module.exports = Backbone.Collection.extend({

  url: '/rest/gis/structures',

  model: ProjectSitesModel,

  fetch: function(options) {
    options = _.defaults((options || {}), {
      type: 'POST',
      data:'{}'
    });
    /*TODO implement manual caching */
    return Backbone.Model.prototype.fetch.call(this, options);
  },

  parse: function (response) {
    //fetch activities
    //window.app.data.activities.fetch();

    //get the unique activities for the structures
    var activeActivityList = _.chain(response.features)
      .pluck('properties')
      .pluck('activity')
      .flatten()
      .unique()
      .value();

    window.app.data.activities.getActivities(activeActivityList);

    return response.features;
  }

});
