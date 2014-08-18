var Deferred = require('jquery').Deferred;
var when = require('jquery').when;
var _ = require('underscore');
var Backbone = require('backbone');

var Palette = require('../../colours/colour-palette');


module.exports = Backbone.Model.extend({

  initialize: function() {

    // private load/processing state tracking
    this._dataLoaded = new Deferred();
    this._boundaryLoaded = new Deferred();
    this._boundaryJoined = new Deferred();  // resolves after processing

    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });

    // TODO: do this later? Maybe set as a model attribute instead of directly
    // on the model? Should it sometimes be Discrete instead of FromRange?
    this.palette = new Palette.FromRange({ seed: this.get('id') });
  },

  load: function() {
    /*
     * Load the data for this layer, load the boundary to join with, and join.
     */
    var self = this;
    if (this._dataLoaded.state() === 'pending') {
      this.fetch().then(self._dataLoaded.resolve);
    }
    if (this._boundaryLoaded.state() === 'pending') {
      var boundaryLink = this.get('joinBoundariesLink');  // TODO: handle IDs vs links consitently
      var boundaryId = boundaryLink.split('gis/boundaries/')[1];  // for now, (for ever?,) they are all local
      var boundary = this.collection.boundaries.find(function(boundary) { return boundary.id === boundaryId; });
      if (! boundary) {  // sanity check
        throw new Error('No boundary found for indicator layer:', this.get('title'));
      }
      boundary.loadGeoJSON()
        .then(function(geoJSON) {
          self._boundaryLoaded.resolve(geoJSON);
        });
    }

    this._dataLoaded.then(function() {
      self.trigger('loaded', self);
      self.updatePaletteRange();
    });

    when(this._boundaryLoaded, this._dataLoaded).then(function(geoJSON) {
      // TODO: passing the geoJSON through here is sketch
      self._joinDataWithBoundaries(geoJSON);
    });

    this._boundaryJoined.then(function() {
      self.trigger('processed', self);
    });

    return this._dataLoaded.promise();
  },

  updatePaletteRange: function() {
    var min = +Infinity,
        max = -Infinity;
    _.each(this.get('data').values, function(value) {
      if (value.value < min) {
        min = value.value;
      }
      if (value.value > max) {
        max = value.value;
      }
    });
    this.palette.set({min: min, max: max});
  },

  _joinDataWithBoundaries: function(boundaryGeoJSON) {
    if (this._boundaryJoined.state() === 'pending') {
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
      this._boundaryJoined.resolve(geoJSON);
    }
    return this._boundaryJoined.promise();
  },

});
