var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Activity = require('../models/activity-model');

/*Backbone Collection Activities (TODO RENAME FILE)*/
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({

  url: '/rest/gis/activities',
  model: Activity,
  type: 'POST',
  filter: null,
  settings: null,
  appData: null,

  initialize: function(models, options) {
    this.appData = options.appData;
  },

  fetch: function(options) {

    var payload = {otherFilters: {}};
    /* TODO nice to have: if otherFilters and columnFilters
     * had their own object on API, separate from settings, etc.
     * Currently all on the same data level.
     **/
    /* get filters if set (not applicable for getActivities) */
    if (this.appData.filter) {
      _.extend(payload, this.appData.filter.serialize());
    }

    /* get "settings" */
    if (this.appData.settings) {
      payload.settings = this.appData.settings.serialize();
    }

    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(payload)
    });

    return Backbone.Collection.prototype.fetch.call(this, options);
  },

  parse: function(apiData) {
    return apiData.activities;
  },

  //smart ID fetching, load locally, and only fetch if we don't have the activity.
  getActivities: function(aryOfIDs) {
    var deferred = $.Deferred();

    // check which IDs we have locally.
    var matches = [];
    this.each(function(activity) {
      var index = _.indexOf(aryOfIDs, activity.id);
      if (index > -1) {
        matches.push(activity.toJSON());   // add activity to array
        aryOfIDs.splice(index, 1);         // remove id from array
      }
    });

    if (!_.isEmpty(aryOfIDs)) {
      // do an api request to get remaining ones
      this.url = '/rest/gis/activities/' + aryOfIDs.join(',');
      this.fetch({
        remove: false,
        filter: null
      })
      .then(function(newData) {
        var activities = newData.activities;
        matches = _.union(matches, activities);
        deferred.resolve(matches);
      })
      .fail(function(err) {
        console.error('failed to get ' + this.url, err);
        deferred.resolve(matches);
      });
    } else {
      deferred.resolve(matches);
    }

    this.url = '/rest/gis/activities';  // reset url, inside deferred fails

    return deferred;
  }

});
