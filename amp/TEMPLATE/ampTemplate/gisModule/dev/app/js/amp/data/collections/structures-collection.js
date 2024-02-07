var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var Palette = require('../../colours/colour-palette');
var StructureModel = require('../models/structure-model');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Activities = require('../collections/activity-collection');

/* Structures collection
 * Structures have longitude and latitude and belong to one or more
 * activities (aka Projects) but are not a type of activity.
 *
 **/
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({

  url: '/rest/gis/structures',
  model: StructureModel,
  filter: null,
  settings: null,
  appData: null,

  initialize: function(models, options) {
    if (options) {
      this.filter = options.filter;
      this.settingsWidget = options.settingsWidget;
      this.appData = options.appData;
      this.activities = new Activities([], options);
      this._joinedActivities = null;
      this._lastFetch = null;
      this.palette = new Palette.FromSet();

    } else {
      console.warn('Structures colln: no options were provided for context');
    }

    _.bindAll(this, 'fetch', 'updatePaletteSet', 'getStructuresWithActivities', '_getActivityIds');

  },

  fetch: function(options) {
    // TODO: this is running twice on structures load... is that ok??
    var self = this;
    var payload = {};

    // cancel last request if not complete.
    if (this._lastFetch && this._lastFetch.readyState > 0 && this._lastFetch.readyState < 4) {
      this._lastFetch.abort();
    }

    /* get filters if set (not applicable for getActivities) */
    if (this.appData.filter) {
      _.extend(payload, this.appData.filter.serialize());
    }

     if (this.appData.performanceToggleModel.get('isPerformanceToggleSelected') != null) {
        payload['performanceIssues'] = !this.appData.performanceToggleModel.get('isPerformanceToggleSelected');
     }
    
    /* get "settings" */
    // TODO: re-enable?? check for listener....?
    /*if (this.appData.settings) {
      payload.settings = this.appData.settings.serialize();
    }*/

    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(payload)
    });

    /*TODO implement manual caching */
    this._lastFetch = Backbone.Collection.prototype.fetch.call(this, options).then(function() {
      self._joinActivities();
    });

    return this._lastFetch;
  },

  //always does a fresh fetch on structures.
  fetchStructuresWithActivities: function() {
    this._joinedActivities = $.Deferred();
    this.fetch();
    return this._joinedActivities;
  },

  //doesn't encourage a fresh fetch
  getStructuresWithActivities: function() {
    if (this._joinedActivities) {
      return this._joinedActivities;
    } else {
      this._joinedActivities = $.Deferred();
      this.load();
      return this._joinedActivities;
    }
  },

  // TODO force / wait for activities to finish joining with filters...
  _joinActivities: function() {
    var self = this;

    // watch it: this next call is _expensive_.
    this.activities.getActivities(this._getActivityIds()).then(function() {

      // Do actual join
      self.each(function(structure) {  // TODO: this is currently expensive, and can be optimized.
        var activity = structure.get('activity');

        // activity.attributes is a dirty way of checking if already a model...
        // if not joined yet, then join structure to parent activity.

        if (!(activity && activity.attributes)) {
          var match = self.activities.find(function(model) {
            return model.id === structure.get('activityZero');
          });

          if (match) {
        	  match.joinFilters();
              structure.set('activity', match);
          }

        } else if (activity.attributes) {
        	activity.joinFilters();
        }


      });

      // all activites joined filters
      self.updatePaletteSet();
      self._joinedActivities.resolve();
    });

    return self._joinedActivities;
  },

  _getActivityIds: function() {
    // reduces to a single unique list
    return this
      .chain()
      .reduce(function(memo, structure) {
        memo.push(structure.get('activityZero'));
        return memo;
      }, [])
      .uniq()
      .value();
  },

  toGeoJSON: function() {
    var featureList = this.chain()
	 .filter(function(model) {
	     return model.get('activity') !== null && !_.isArray(model.get('activity'));
	  })
	  .map(function(model) {
      return {
        type: 'Feature',
        geometry: {
          type: model.get('geometryType'),
          coordinates: model.get('coordinates'),
        },
        properties: model.attributes  // not toJSON() for performance
      };

    }).value();

    return {
      type: 'FeatureCollection',
      features: featureList
    };
  },

  // Migrated from Collection-Model
  updatePaletteSet: function() {
    var self = this;
    var deferred = $.Deferred();
    var filterVertical = self.appData.structuresMenu.get('filterVertical');

    // load the necessary activities.
    this.getStructuresWithActivities().done(function() {
      // TODO: this is running twice on structures load?!?
      var orgSites = self.chain()
      .filter(function(structure) {
    	     return structure.get('activity') !== null && !_.isArray(structure.get('activity'));
    	 })
        .groupBy(function(site) {

          var activity = site.get('activity');
          var filterVerticalText;

          if (filterVertical === 'Primary Sector')
          {
            filterVerticalText='Sectors';
          }
          else if (filterVertical === 'Programs')
            filterVerticalText='Programs'
          else
            filterVerticalText='Donors'
          // TODO: Choosing a vertical will need to be configurable from drop down..
          if (!_.isEmpty(activity.get('matchesFilters')[filterVertical])) {
            if (activity.get('matchesFilters')[filterVertical].length > 1) {

              var localizedMultipleItem = [
                  '<span data-i18n="amp.gis:legend-multiple-', filterVerticalText.toLowerCase(), '">',
                  'Multiple ',
                  filterVerticalText,
                  '</span>'
                ].join('');
              return localizedMultipleItem;

            } else if (activity.get('matchesFilters')[filterVertical][0].get) {
              var name = activity.get('matchesFilters')[filterVertical][0].get('name');
              return name;
            } else {
              console.warn('matchFilters are not models.', activity.get('matchesFilters'));
              return '';
            }
          } else {
            //In cases where projects do not have sectors or donors (for example draft projects
            //or planned project with no funding) there needs to be a 'none' option for project sites
            //in the GIS legends
            if (activity.get('matchesFilters')[filterVertical] !== undefined) {
              return this.app.translator.translateSync ('amp.gis:legend-none', 'None');
            } else {
              console.warn('Activity is missing desired vertical');
              return this.app.translator.translateSync ('amp.gis:legend-na', 'n/a');
            }
          }
        })
        .map(function(sites, orgId) {
          var code = -1;
          if (_.has(sites[0].get('activity').get('matchesFilters'), filterVertical)) {
            if (sites[0].get('activity').get('matchesFilters')[filterVertical] === null) {
              //no value for sector/donor
              code = '1';
            } else if (sites[0].get('activity').get('matchesFilters')[filterVertical][0].get &&
                        sites[0].get('activity').get('matchesFilters')[filterVertical].length > 1) {
              code = '0';
            } else {
              if ((sites[0].get('activity').get('matchesFilters')[filterVertical][0] instanceof Object) &&
            		  sites[0].get('activity').get('matchesFilters')[filterVertical][0].get) {
                code = sites[0].get('activity').get('matchesFilters')[filterVertical][0].get('code');
              } else {
                code = sites[0].get('activity').get('matchesFilters')[filterVertical][0];
              }
            }
          }

          return {
            id: orgId,
            name: orgId,
            code: code,
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
  },

  parse: function(response) {
    return response.features;
  }



});
