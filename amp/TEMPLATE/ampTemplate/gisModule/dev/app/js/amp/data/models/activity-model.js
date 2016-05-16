var Backbone = require('backbone');
var $ = require('jquery');
var _ = require('underscore');

module.exports = Backbone.Model.extend({
  idAttribute: 'Activity Id',
  defaults: {
    name: 'Untitled Activity',
    ampUrl: '/aim/viewActivityPreview.do~public=true',
    matchesFilters: {
      //'Donor Agency': ''
    }
  },

  initialize: function() {
    this._joinComplete = this._joinFilters();
  },

  // currently we use: tempDirtyForceJoin instead, but should fix it.
  getJoinedVersion: function() {
    return this._joinComplete;
  },

  // created this because the joins were being overriden after the initialize, not sure how/why
  // ideally track down why and this won't be needed, then we can just use: getJoinedVersion
  tempDirtyForceJoin: function() {
    return this._joinFilters();
  },

  _joinFilters: function() {
    var self = this;
    var deferred = $.Deferred();

    this.collection.appData.filter.getAllFilters().then(function(allFilters) {
      var matchesFilters = self.attributes.matchesFilters;
      if (allFilters && allFilters.columnFilters && matchesFilters) {
        _.each(matchesFilters, function(v, k) {
          var filterKey = k;
      	  if('Primary Sector' === k){
      		  filterKey = 'Primary Sector Id';
      	  }else if('Donor Agency' === k){
      		  filterKey = 'Donor Id';
      	  }  
          //make sure it's a valid filter
          if (allFilters.columnFilters[filterKey]) {
            //iterate over ids.        	        			  
            _.each(matchesFilters[k], function(id, index) {

              var matched = _(allFilters.columnFilters[filterKey]).findWhere({id: id});
              if (matched) {
                matchesFilters[k][index] = matched;
              }
            });
          }
        });
        self.set('matchesFilters', matchesFilters);
      }
      deferred.resolve();
    });

    return deferred;
  },

  parse: function(data) {
    // make our id an int
    data['Activity Id'] = parseInt(data['Activity Id'], 10);    
    return data;
  },

  // Use to hook in before template calls.
  toJSON: function() {
    var json = _.clone(this.attributes);

    json.donorNames = this._getDonorNames();
    json.sectorNames = this._getSectorNames();

    return json;
  },

  _getDonorNames: function() {
    var matchesFilters = this.attributes.matchesFilters;
    if (matchesFilters && matchesFilters['Donor Agency']) {
      if (matchesFilters['Donor Agency'].length > 1) {
        return 'Multiple';
      } else if (matchesFilters['Donor Agency'][0] && matchesFilters['Donor Agency'][0].attributes) {
        return matchesFilters['Donor Agency'][0].get('name');
      }
    }
    return '';
  },


  _getSectorNames: function() {
    var matchesFilters = this.attributes.matchesFilters;
    if (matchesFilters && matchesFilters['Primary Sector']) {
      if (matchesFilters['Primary Sector'].length > 1) {
        return 'Multiple';
      } else if (matchesFilters['Primary Sector'][0] && matchesFilters['Primary Sector'][0].attributes) {
        return matchesFilters['Primary Sector'][0].get('name');
      }
    }
    return '';
  }
});
