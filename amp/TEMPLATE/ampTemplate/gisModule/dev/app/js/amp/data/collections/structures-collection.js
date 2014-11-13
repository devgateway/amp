var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var Palette = require('../../colours/colour-palette');
var ProjectSiteModel = require('../models/structure-model');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Activities = require('../collections/activity-collection');
//var ActivityCollection = require('../collections/activity-collection');

/* ProjectSites (a.k.a Structures) collection
 * ProjectSites have longitude and latitude and belong to one or more
 * activities (aka Projects) but are not a type of activity.
 *
 **/
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({

  url: '/rest/gis/structures',
  model: ProjectSiteModel,
  filter: null,
  settings: null,
  appData: null,

  initialize: function(models, options) {
    if (options) {
      this.filter = options.filter;
      this.settings = options.settings;
      this.appData = options.appData;
      this.activities = new Activities([], options);
      this._joinedActivities = null;
      this._lastFetch = null;

      this.palette = new Palette.FromSet();

    } else {
      console.warn('Project Sites/Structures colln: no options were provided for context');
    }

    _.bindAll(this, 'fetch', 'updatePaletteSet', 'getStructuresWithActivities', '_getActivityIds');

  },

  fetch: function(options) {
    var self = this;
    var payload = {otherFilters: {}};

    //cancel last request if not complete.
    if (this._lastFetch && this._lastFetch.readyState > 0 && this._lastFetch.readyState < 4) {
      this._lastFetch.abort();
    }

    /* TODO nice to have: if otherFilters and columnFilters
     * had their own object on API, separate from settings, etc.
     * Currently all on the same data level.
     **/
    /* get filters if set (not applicable for getActivities) */
    if (this.appData.filter) {
      _.extend(payload, this.appData.filter.serialize());
    }

    /* get "settings" */
    /*if (this.appData.settings) {
      payload.settings = this.appData.settings.serialize();
    }*/

    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(payload)
    });

    /*TODO implement manual caching */
    this._lastFetch = Backbone.Collection.prototype.fetch.call(this, options).then(function() {
      self._joinedActivities = self._joinActivities();
      self.updatePaletteSet();
    });

    return this._lastFetch;
  },

  getStructuresWithActivities: function() {
    var self = this;
    if (this._joinedActivities) {
      return this._joinedActivities;
    } else {
      var tmpDeferred = $.Deferred();
      this.load().then(function() { self._joinedActivities.then(function() {tmpDeferred.resolve(); }); });
      return tmpDeferred;
    }
  },

  parse: function(response) {
    return response.features;
  },

  //TODO force / wait for activities to finish joining with filters...
  _joinActivities: function() {
    var self = this;
    var deferred = $.Deferred();

    this.activities.getActivities(this._getActivityIds()).then(function() {

      //Do actual join
      self.each(function(structure) {
        //dirty way of checking if already a model...
        if (!(structure.get('activity') && structure.get('activity').attributes)) {
          var match = self.activities.find(function(model) {
            //intentionally double ==
            return model.id ==  structure.get('activityZero'); //intentionally double ==
          });

          structure.set('activity', match);
        } else {
          console.log('no activity');
        }
      });
      deferred.resolve();
    });

    return deferred;
  },

  _getActivityIds: function() {
    // reduces to a single unique list
    return this.reduce(function(memo, structure) {
      if (!memo) {
        memo = [];
      }
      memo.push(structure.get('activityZero'));
      return _.uniq(memo);
    }, []);
  },

  toGeoJSON: function() {
    var featureList = this.map(function(model) {
      return {
        type: 'Feature',
        geometry: {
          type: 'Point',
          coordinates: [model.get('lng'), model.get('lat')]
        },
        properties: model.toJSON()
      };
      /*TODO(thadk): move to model and use return feature.toGeoJSON();*/
    });
    return {
      type: 'FeatureCollection',
      features: featureList
    };
  },

// Migrated from Collection-Model
  updatePaletteSet: function() {
    var self = this;
    var deferred = $.Deferred();

    //load the necessary activities.
    this.getStructuresWithActivities().done(function() {
      var orgSites = self.chain()
        .groupBy(function(site) {
          var activity = site.get('activity');

          // TODO:  for now we want just organizations[1]  for donor.
          // Choosing a vertical will need to be configurable from drop down..
          if (!_.isEmpty(activity.get('matchesFilters')['Donor Id'])) {
            if (activity.get('matchesFilters')['Donor Id'].length > 1) {
              return -1; //multiple
            } else {
              return activity.get('matchesFilters')['Donor Id'];
            }
          } else {
            console.warn('Activity is missing desired vertical');
            return -1;
          }
        })
        .map(function(sites, orgId) {
          return {
            id: orgId,
            name: orgId, //TODO: get org name from join/filters
            sites: _(sites).map(function(site) { return site.get('id'); })
          };
        })
        .sortBy(function(item) {
          return item.sites.length;
        })
        .reverse()
        .value();

      self.palette.set('elements', orgSites);

      deferred.resolve();
      self.trigger('refresh', this);

    });

    return deferred;
  }



});
