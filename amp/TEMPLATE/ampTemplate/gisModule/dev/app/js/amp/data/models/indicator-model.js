var ajax = require('jquery').ajax;
var Deferred = require('jquery').Deferred;
var when = require('jquery').when;
var _ = require('underscore');
var Backbone = require('backbone');


module.exports = Backbone.Model.extend({

  defaults: {
    title: undefined,
    type: undefined
  },

  initialize: function() {
    if (this.get('type') === 'joinBoundaries') {
      this.initJoinLayer();
      this.load = this.loadJoinLayer;
    }
  },

  load: function() {
    // initialize overwrites this method with the right one for the layer type
    console.warn('no load method for this layer', this);
    return new Deferred().fail(this);
  },

  initJoinLayer: function() {
    var self = this;
    this._dataLoadDeferrer = new Deferred();
    this._boundaryLoadDeferrer = new Deferred();
    this._boundaryJoinDeferrer = new Deferred();
    this.set('geoJSON', undefined);  // just make the attribute exist for now
  },

  loadJoinLayer: function() {
    /*
     * Load the data for this layer, load the boundary to join with, and join.
     *
     * This method is asynchronous, and there are two ways to follow up:
     * 1. Attach a callback with the returned promise
     * 2. Listen on this model for change:geoJSON
     */
    if (this.get('type') !== 'joinBoundaries') {
      throw new Error('loadJoinLayer is for "joinBoundaries" layers, this: ', this);
    }
    var self = this;
    if (this._dataLoadDeferrer.state() === 'pending') {
      this.fetch().then(self._dataLoadDeferrer.resolve);
    }
    if (this._boundaryLoadDeferrer.state() === 'pending') {
      var boundaryLink = this.get('joinBoundariesLink');  // TODO: handle IDs vs links consitently
      var boundaryId = boundaryLink.split('gis/boundaries/')[1];  // for now, (for ever?,) they are all local
      var boundary = this.collection.boundaries.find(function(boundary) { return boundary.id === boundaryId; });
      if (! boundary) {  // sanity check
        throw new Error('No boundary found for indicator layer:', this.get('title'));
      }
      boundary.loadGeoJSON()
        .then(function(geoJSON) { self._boundaryLoadDeferrer.resolve(geoJSON); });
    }
    when(this._boundaryLoadDeferrer, this._dataLoadDeferrer)
      .then(function(geoJSON) { self._joinDataWithBoundaries(geoJSON); });
    return this._boundaryJoinDeferrer.promise();
  },

  _joinDataWithBoundaries: function(boundaryGeoJSON) {
    if (this._boundaryJoinDeferrer.state() === 'pending') {
      var indexedValues = _.indexBy(this.get('data').values, 'boundariesFeatureId');
      // copy boundary geoJSON, and inject data
      var geoJSON = _.extend({}, boundaryGeoJSON, {
        features: _.map(boundaryGeoJSON.features, function(feature) {
          // replace boundary properties with {value: value}
          // TODO... keep the existing properties and just add value?
          // replacing for now, to save weight
          return _.extend(feature, {
            properties: {
              value: indexedValues[feature.id].value
            }
          });
        })
      });
      this.set('geoJSON', geoJSON);
      this._boundaryJoinDeferrer.resolve(geoJSON);
    }
    return this._boundaryJoinDeferrer.promise();
  }

});
