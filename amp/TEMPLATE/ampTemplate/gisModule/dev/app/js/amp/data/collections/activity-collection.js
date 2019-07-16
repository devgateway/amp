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
    this.globalPageSize = options.pageSize;
    this.totalCount = null;
    this._currentStartPosition = 0;
  },

  /* If _pageSize > 0 then use pagination and fetchMore, otherwise ignore pagination */
  fetch: function(options) {
    var self = this;
    var payload = {};
    var activityFetch;
    var isFetchMore = false;
    var preserveURL = this.url;

    /* get filters if set (not applicable for getActivities) */
    if (this.appData.filter) {
      _.extend(payload, this.appData.filter.serialize());
    }

    /* include "settings", only if there is something to send. DO NOT send blank settings. */
    if (this.appData.settingsWidget && !_.isEmpty(this.appData.settingsWidget.toAPIFormat())) {
      payload.settings = this.appData.settingsWidget.toAPIFormat();
    }

    if (this.appData.performanceToggleModel.get('isPerformanceToggleSelected') != null) {
    	payload['performanceIssues'] = !this.appData.performanceToggleModel.get('isPerformanceToggleSelected');	
    }
    
    /* These will always need to be reset when you do a raw fetch
     * like for new filters or settings
     **/
    if (options && options.isFetchMore) {
    	this._currentStartPosition += options.pageSize;
      isFetchMore = true;
    } else {
      this._currentStartPosition = 0;
    }

    /* Only use pagination if _pageSize is positive */
    if (this.globalPageSize && this.globalPageSize > 0) {

      /* POST payload-specified size and start are currently ignored by API but
       * they would be cleaner than modifing and restoring the URL
       */
      /*
       * payload.size = this._pageSize;
       * payload.start = this._currentStartPosition;
       */

      this.url =
       [
          '/rest/gis/activities?start=',
         this.getPageDetails().currentPage,
         '&size=', 
         this.globalPageSize
       ].join('');
    }

    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(payload)
    });

    activityFetch = Backbone.Collection.prototype.fetch.call(this, options);

    /* If enabled, maintain the pagination state even on errors */
    if (options.pageSize && options.pageSize > 0) {

      activityFetch.then(function() {
        /* On the very first page request, advance if succeeds */
        if (options && !options.isFetchMore) {
        	self._currentStartPosition += options.pageSize;
        }
      }).fail (function() {
        /* If a page request after the first fails, revert position*/
        if (self.isFetchMore) {
        	self._currentStartPosition -= options.pageSize;
        }
      });

      this.url = preserveURL;
    }

    return activityFetch;
  },

  /* Used for pagination */
  fetchMore: function(options) {
    options = _.defaults((options || {}), {
      isFetchMore: true,
      pageSize: this.globalPageSize,
      remove: false
    });
    return this.fetch(options);

  },

  /* Used for pagination */
  getPageDetails: function() {
    var pageDetails =  {
      isPaging: this.globalPageSize > 0,
      pageSize: this.globalPageSize,
      currentPosition: this._currentStartPosition,
      totalCount: this.totalCount,
      url: this.url
    };

    if (pageDetails.isPaging) {
    	pageDetails.currentPage = Math.floor(this._currentStartPosition / pageDetails.pageSize);
    	pageDetails.totalPageCount = Math.ceil(this.totalCount / pageDetails.pageSize);
    }
    return pageDetails;
  },

  /* changePaging is like reinitializing without paging, requiring everything to refetch
  * as if paging was never enabled.*/
  /*this function is never used, commenting out to avoid confusion*/
  /*
  changePaging: function(resultsPerPage) {
    this._pageSize = resultsPerPage || 0;
  },
  */

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
      this.url = '/rest/gis/activities?' + aryOfIDs.map(function(id) { return "id="+id; }).join('&');
      var payload = {};
      if (this.appData.filter) {
        _.extend(payload, this.appData.filter.serialize());
      }
      if (this.appData.settingsWidget && !_.isEmpty(this.appData.settingsWidget.toAPIFormat())) {
        payload.settings = this.appData.settingsWidget.toAPIFormat();
      }
      
      if (this.appData.performanceToggleModel.get('isPerformanceToggleSelected') != null) {
    	  payload['performanceIssues'] = !this.appData.performanceToggleModel.get('isPerformanceToggleSelected'); 
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
  },
  
    //Gets the activities with the ids in the aryOfIDs array. 
	// Location percentage is applied to the funding amounts.
	// Does not cache the activities.
   getActivitiesforLocation : function(aryOfIDs, admLevel, admId) {
		var self = this;
		var deferred = $.Deferred();
		var matches = [];
		if (!_.isEmpty(aryOfIDs)) {
			var payload = {};
			if (this.appData.filter) {
				_.extend(payload, this.appData.filter.serialize());
			}

			if (this.appData.settingsWidget && !_.isEmpty(this.appData.settingsWidget.toAPIFormat())) {
				payload.settings = this.appData.settingsWidget.toAPIFormat();
			}

			if (this.appData.performanceToggleModel.get('isPerformanceToggleSelected') != null) {
				payload['performanceIssues'] = !this.appData.performanceToggleModel.get('isPerformanceToggleSelected');
			}

			if (admId != null && admLevel != null) {
				payload.filters[admLevel.toLowerCase()] = [ admId ];
			}

			var TempActivityCollection = Backbone.Collection.extend({
				model : Activity,
				initialize : function(models, options) {
					this.appData = options.appData;
				},
				parse : function(apiData) {
					this.totalCount = apiData.count;
					return apiData.activities;
				}
			});

			var activities = new TempActivityCollection([], {
				appData : this.appData
			});
			activities.url = '/rest/gis/activities?' + aryOfIDs.map(function(id) { return "id="+id; }).join('&');
			activities.fetch({
				remove : false,
				data : JSON.stringify(payload),
				type : 'POST'
			}).then(function() {
				activities.each(function(activity) {
					matches.push(activity); // add activity to array           
				});

				deferred.resolve(matches);
			}).fail(function(err) {
				console.error('failed to get ' + this.url, err);
				deferred.resolve(matches);
			});
		} else {
			deferred.resolve(matches);
		}

		return deferred;

	}

});
