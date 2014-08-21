var when = require('jquery').when;
var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Palette = require('../../colours/colour-palette');


module.exports = Backbone.Model
  // .extend(LoadOnceMixin)  // not needed: load is called directly
  .extend({

  initialize: function() {

    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });

    // TODO: do this later? Maybe set as a model attribute instead of directly
    // on the model? Should it sometimes be Discrete instead of FromRange?
    this.palette = new Palette.FromRange({ seed: this.get('id') });
  },

  load: function() {
    var loaded = LoadOnceMixin.load.apply(this);
    loaded.done(function() {
      this.trigger('loaded', this);  // LEGACY
    });

    this.loadBoundary(loaded);
    return loaded;
  },

  loadBoundary: function(dataLoaded) {
    var boundaryLink = this.get('joinBoundariesLink');  // TODO: handle IDs vs links consitently
    var boundaryId = boundaryLink.split('gis/boundaries/')[1];  // for now, (for ever?,) they are all local
    var boundary = this.collection.boundaries.find(function(boundary) { return boundary.id === boundaryId; });
    if (! boundary) {  // sanity check
      throw new Error('No boundary found for indicator layer:', this.get('title'));
    }

    var boundaryLoaded = boundary.load();

    when(boundaryLoaded, dataLoaded)         // Order is important...
      .done(function(boundaryModel, self) {  // ...args follow "when" order
        self._joinDataWithBoundaries(boundaryModel.toJSON());
        self.trigger('processed');
      });
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
  }

});
