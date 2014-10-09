var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Activity = require('../models/activity-model');

module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({

  url: '/rest/gis/activities',
  model: Activity,
  type: 'POST',

  fetch: function(options) {
    options = _.defaults((options || {}), {
      type: 'POST',
      data:'{}'
    });
    return Backbone.Model.prototype.fetch.call(this, options);
  },


  //smart ID fetching, load locally, and only fetch if we don't have the activity.
  getActivites: function(aryOfIDs) {
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
      this.fetch({remove: false, type: 'GET', data:''}).then(function(newData) {
        matches = _.union(matches, newData);
        deferred.resolve(matches);
      }).fail(function(err) {
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
