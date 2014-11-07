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
  totalCount: null,
  _pageSize: 0,
  _currentStartPosition:0,

  initialize: function(models, options) {
    this.appData = options.appData;
    this._pageSize = options.pageSize;
  },

  fetch: function(options) {

    var self = this;
    var payload = {otherFilters: {}};
    var tempDeferred;
    var isFetchMore = false;
    var preserveURL = this.url;
    /* TODO nice to have: if otherFilters and columnFilters
     * had their own object on API, separate from settings, etc.
     * Currently all on the same data level.
     **/
    /* get filters if set (not applicable for getActivities) */
    if (this.appData.filter) {
      _.extend(payload, this.appData.filter.serialize());
    }

    /* include "settings", only if there is something to send. DO NOT send blank settings. */
    if (this.appData.settings && !_.isEmpty(this.appData.settings.serialize())) {
      payload.settings = this.appData.settings.serialize();
    }

    if (this._pageSize > 0) {
      payload.size = this._pageSize;
    }

    /*These will always need to be reset when you do a raw fetch
    * like for new filters or settings */
    if (options && options.isFetchMore) {
      this._currentStartPosition += this._pageSize;
      isFetchMore = true;
    } else {
      this._currentStartPosition = 0;
    }



    if (this._pageSize > 0) {
      payload.start = this._currentStartPosition;
      this.url = ['/rest/gis/activities?start=', this._currentStartPosition, '&size=', this._pageSize].join('');
    }

    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(payload)
    });

     //run fetch before reverting URL
    tempDeferred = Backbone.Collection.prototype.fetch.call(this, options);

    tempDeferred.then(function() {
      if (self._pageSize > 0) {
        if (options && !options.isFetchMore) {
          self._currentStartPosition += self._pageSize;
        }
      }
    }).fail (function() {
      /* if fails, revert current position*/
      if (self._pageSize > 0 && self.isFetchMore) {
        self._currentStartPosition -= self._pageSize;
      }
    });
    this.url = preserveURL;

    return tempDeferred;
  },

  fetchMore: function(options) {
    options = _.defaults((options || {}), {
      isFetchMore:true,
      remove: false
    });
    return this.fetch(options);

  },

  getPageDetails: function() {
    var pageDetails =  {
      isPaging: this._pageSize > 0,
      pageSize: this._pageSize,
      currentPosition: this._currentStartPosition,
      totalCount: this.totalCount,
      url: this.url
    };

    if (pageDetails.isPaging) {
      pageDetails.currentPage = Math.floor(this._currentStartPosition / this._pageSize);
      pageDetails.totalPageCount = Math.ceil(this.totalCount / this._pageSize);
    }
    return pageDetails;
  },

  /* changePaging is like reinitializing without paging, requiring everything to refetch
  * as if paging was never enabled.*/
  changePaging: function(resultsPerPage) {
    this._pageSize = resultsPerPage || 0;
  },

  parse: function(apiData) {
    this.totalCount = apiData.count;
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
