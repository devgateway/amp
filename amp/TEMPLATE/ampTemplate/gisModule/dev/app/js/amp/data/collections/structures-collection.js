var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var ProjectSiteModel = require('../models/structure-model');
//var ActivityCollection = require('../collections/activity-collection');

/* ProjectSites (a.k.a Structures) collection
 * ProjectSites have longitude and latitude and belong to one or more
 * activities (aka Projects) but are not a type of activity.
 *
 **/
module.exports = Backbone.Collection.extend({

  url: '/rest/gis/structures',
  model: ProjectSiteModel,
  activities: null,
  filter: null,
  settings: null,
  appData: null,

  initialize: function(models, options) {
    if (options) {
      this.activities = options.activities;
      this.filter = options.filter;
      this.settings = options.settings;
      this.appData = options.appData;
    } else {
      console.warn('Project Sites/Structures colln: no options were provided for context');
    }
  },

  fetch: function(options) {
    options = _.defaults((options || {}), {
      type: 'POST',
      data:'{}'
    });

    /*TODO implement manual caching */

    /* Be sure to be matching the Backbone.Collection or Backbone.Model prototype below */
    return Backbone.Collection.prototype.fetch.call(this, options);
  },

  parse: function(response) {
    //fetch ALL activities
    //window.app.data.activities.fetch();

    /* default: {
     *  type: "FeatureCollection",
     *  features: []
     * }
     */

    //get the list of unique activities for the structures
    var activeActivityList = _.chain(response.features)
      .pluck('properties')
      .pluck('activity')
      .flatten()
      .unique()
      .value();

    /* TODO(thadk): find a more encapsulated path to communicate this promise to model's map function */
    /* use options.app instead of window.app  -- also consider options.collection as this */
    /* window.app.data.relevantActivitesFetch = window.app.data.activities.getActivities(activeActivityList);*/

    return response.features;
  },

/*Migrated from Collection-Model */
  getSelected: function() {
    return _.chain(this.get('selected') ? [this] : []);
  },

/*Migrated from Collection-Model */
  updatePaletteSet: function() {
    var deferred = $.Deferred();
    var self = this;

    //load the necessary activities.
    this.loadAll().done(_.bind(function() {
      var activity;

      var orgSites = this.get('sites')
        .chain()
        .groupsBy(function(site) {

          if (!_.isEmpty(self.activities.get(site.get('properties').activity))) {
            // doesn't handle multiple activities, which may be introduced in the future..
            activity = self.activities.get(site.get('properties').activity[0]);

            // TODO:  for now we want just organizations[1]  for donor.
            // Choosing a vertical will need to be configurable from drop down..
            if (!_.isEmpty(activity.get('matchesFilters').organizations['1'])) {
              return activity.get('matchesFilters').organizations['1'];
            } else {
              console.warn('Activity is missing desired vertical');
              return -1;
            }
          } else {
            console.warn('Structure is missing an activity');
            return -1;
          }
        })
        .map(function(sites, orgId) {
          return {
            id: orgId,
            name: ONAMES[orgId], // TODO: use filters for lookup once we have app.data.filters
            sites: _(sites).map(function(site) { return site.get('id'); })
          };
        })
        .sortBy(function(item) {
          return item.sites.length;
        })
        .reverse()
        .value();

      this.palette.set('elements', orgSites);
      deferred.resolve();

    }, this));

    return deferred;
  }



});
