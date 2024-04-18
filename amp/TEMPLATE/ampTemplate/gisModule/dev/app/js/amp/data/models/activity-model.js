var Backbone = require('backbone');
var $ = require('jquery');
var _ = require('underscore');
var Constants = require('../../../libs/local/constants.js');

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
    this._joinComplete = this.joinFilters();
  },

  // currently we use: tempDirtyForceJoin instead, but should fix it.
  getJoinedVersion: function() {
    return this._joinComplete;
  },

  // created this because the joins were being overriden after the initialize, not sure how/why
  // ideally track down why and this won't be needed, then we can just use: getJoinedVersion
  tempDirtyForceJoin: function() {
    return this.joinFilters();
  },

  joinFilters: function() {
    var self = this;
    var deferred = $.Deferred();

    var filter = this.appData ? this.appData.filter : this.collection.appData.filter;

    filter.getAllFilters().then(function(allFilters) {
    	var matchesFilters = self.attributes.matchesFilters;
    	if (allFilters.filters && matchesFilters) {
    		_.each(matchesFilters, function(v, k) {
    			if (k === Constants.PRIMARY_SECTOR) {
    				_.each(matchesFilters[k], function(sector, index) {
    				   if (!(sector instanceof Backbone.Model)) {
    					   matchesFilters[k][index] = new Backbone.Model(sector);
    				   }

    				});
    			}
                else if (k === Constants.PROGRAMS) {
                    _.each(matchesFilters[k], function(program, index) {
                        if (!(program instanceof Backbone.Model)) {
                            matchesFilters[k][index] = new Backbone.Model(program);
                        }
                    });
                }

                else {
    				//make sure it's a valid filter
    				var filterId  = k.toLowerCase().replace(' ', '-');
    				if (allFilters.filters[filterId]) {
    					//iterate over ids.
    					_.each(matchesFilters[k], function(id, index) {
    						var matched = _(allFilters.filters[filterId]).findWhere({id: id});
    						if (matched) {
    							matchesFilters[k][index] = matched;
    						}
    					});
    				}
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
    json.donorNames = this._getNames(Constants.DONOR_AGENCY);
    json.executingNames = this._getNames(Constants.EXECUTING_AGENCY);
    json.sectorNames = this._getNames(Constants.PRIMARY_SECTOR);
    json.programNames = this._getNames(Constants.PROGRAMS);
    return json;
  },

  _getNames: function(name) {
	var matchesFilters = this.attributes.matchesFilters;
      console.log("Attributes: ",matchesFilters[name])
    if (matchesFilters && matchesFilters[name]) {
      if (matchesFilters[name].length > 1) {
          // let joinedValues = "";
          // for (let i = 0; i < matchesFilters[name].length; i++) {
          //     // Add the current item to the joined string
          //     joinedValues += matchesFilters[name][i]['name'];
          //
          //     // Add a comma if it's not the last element
          //     if (i < matchesFilters[name].length - 1) {
          //         joinedValues += ", ";
          //     }
          // }
          //We could use the joined values but it might be confusing for individual values that have commas in them
        return "Multiple";
      } else if (matchesFilters[name][0] && matchesFilters[name][0].attributes) {
        return matchesFilters[name][0].get('name');
      }

    }
    return 'nil';
  }

});
