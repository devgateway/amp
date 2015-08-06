var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');

var Activity = require('../models/activity-model');

/*Backbone Collection Activities (TODO RENAME FILE) */
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
      this._pageSize = options.pageSize;
      this.totalCount = null;
      this._currentStartPosition = 0;
    },

    /* If _pageSize > 0 then use pagination and fetchMore, otherwise ignore pagination */
    fetch: function(options) {
      var self = this;
      var payload = {otherFilters: {}};
      var activityFetch;
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

      /* These will always need to be reset when you do a raw fetch
       * like for new filters or settings
       **/
      if (options && options.isFetchMore) {
        this._currentStartPosition += this._pageSize;
        isFetchMore = true;
      } else {
        this._currentStartPosition = 0;
      }

      /* Only use pagination if _pageSize is positive */
      if (this._pageSize > 0) {

        /* POST payload-specified size and start are currently ignored by API but
         * they would be cleaner than modifing and restoring the URL
         */
        /*
         * payload.size = this._pageSize;
         * payload.start = this._currentStartPosition;
         */

        this.url = ['/rest/gis/activities?start=',
          this.getPageDetails().currentPage,
          '&size=', this._pageSize].join('');
      }

      options = _.defaults((options || {}), {
        type: 'POST',
        data: JSON.stringify(payload)
      });

      activityFetch = Backbone.Collection.prototype.fetch.call(this, options);

      /* If enabled, maintain the pagination state even on errors */
      if (self._pageSize > 0) {

        activityFetch.then(function() {
          /* On the very first page request, advance if succeeds */
          if (options && !options.isFetchMore) {
            self._currentStartPosition += self._pageSize;
          }
        }).fail (function() {
          /* If a page request after the first fails, revert position*/
          if (self.isFetchMore) {
            self._currentStartPosition -= self._pageSize;
          }
        });

        this.url = preserveURL;
      }

      return activityFetch;
    },

    /* Used for pagination */
    fetchMore: function(options) {
      options = _.defaults((options || {}), {
        isFetchMore:true,
        remove: false
      });
      return this.fetch(options);

    },

    /* Used for pagination */
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
      var self = this;
      var deferred = $.Deferred();

      // check which IDs we have locally.
      var matches = [];
      this.each(function(activity) {
        //intentional +sign to type coerse!
        var index = _.indexOf(aryOfIDs, +activity.id);
        if (index > -1) {
          matches.push(activity);     // add activity to array
          aryOfIDs.splice(index, 1);  // remove id from array
        }
      });

      if (!_.isEmpty(aryOfIDs)) {
        // do an api request to get remaining ones
        this.url = '/rest/gis/activities/' + aryOfIDs.join(',');
        var payload = {};
        if (this.appData.filter) {
          _.extend(payload, this.appData.filter.serialize());
        }
        if (this.appData.settings && !_.isEmpty(this.appData.settings.serialize())) {
          payload.settings = this.appData.settings.serialize();
        }

        Backbone.Collection.prototype.fetch.call(this, {
          remove: false,
          data: JSON.stringify(payload),
          type: 'POST'
        })
          .then(function() { // TODO: optimize this! currently eating 230ms on Phil's computer
            self.each(function(activity) {
              var index = _.indexOf(aryOfIDs, parseInt(activity.id, 10));
              if (index > -1) {
                matches.push(activity);     // add activity to array
                aryOfIDs.splice(index, 1);  // remove id from array
              }
            });

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
