var $ = require('jquery');
var when = require('jquery').when;
var _ = require('underscore');
var Backbone = require('backbone');
var husl = require('husl');

var TopojsonLibrary = require('../../../libs/local/topojson.js');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Palette = require('../../colours/colour-palette');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({

  initialize: function() {
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });

    var numStops = this.get('classes') || 5;
    var values = this.get('values') || [];

    this.palette = new Palette.FromRange({stops: numStops, seed: this.get('id'), values: values });

    // set color based on ramp, if one is provided.
    if (this.get('colorRamp')) {
      var colorHex = this.get('colorRamp')[0].color; //choose last or first colour from ramp.
      this.palette.set('rootHue', husl.fromHex(colorHex)[0]);//Math.floor(seedrandom(options.seed)() * 360));
    }
  },

  loadBoundary: function() {
    // Phil's ideal way of being able to join with non-hosted boundaries.:
    // var boundaryLink = this.get('joinBoundariesLink');  // TODO: handle IDs vs links consitently
    // var boundaryId = boundaryLink.split('gis/boundaries/')[1];  // for now, (for ever?,) they are all local
    // var boundary = this.collection.boundaries.find(function(boundary) { return boundary.id === boundaryId; });

    var boundary = this.collection.boundaries.findWhere({id: this.get('adminLevel')});
    if (!boundary) {  // sanity check
      throw new Error('No boundary found for indicator layer:', this.get('title'));
    }

    var boundaryLoaded = boundary.load();
    when(boundaryLoaded, this.load())         // Order is important...
      .done(function(boundaryModel, self) {  // ...args follow "when" order
        var topoboundaries = boundaryModel.toJSON();
        var topoJsonObjectsIndex = _.chain(topoboundaries.objects)
                                 .keys()
                                 .first()
                                 .value();
        var boundaries = TopojsonLibrary.feature(topoboundaries, topoboundaries.objects[topoJsonObjectsIndex]);
        self.updatePaletteRange();

        self._joinDataWithBoundaries(boundaries);
      });

    return boundaryLoaded;
  },

  loadAll: function() {
    return when(this.load(), this.loadBoundary()).promise().done(function() {
      $('#map-loading').hide();
    });
  },

  updatePaletteRange: function() {
    var min = +Infinity,
        max = -Infinity;

    _.each(this.get('values'), function(value) {
      if (value.value < min) {
        min = value.value;
      }
      if (value.value > max) {
        max = value.value;
      }
    });
    this.palette.set({min: min, max: max, values: this.get('values')});
  },

  _joinDataWithBoundaries: function(boundaryGeoJSON) {
    var self = this;
    var indexedValues = _.indexBy(this.get('values'), 'geoId');
    var admKey = this.get('adminLevel').replace('-', '').toUpperCase();

    // copy boundary geoJSON, and inject data
    var geoJSON = _.extend({}, boundaryGeoJSON, {
      features: _.map(boundaryGeoJSON.features, function(feature) {
        // replace boundary properties with {value: value}
        // TODO... keep the existing properties and just add value?
        // replacing for now, to save weight
        feature.id = feature.properties[admKey + '_CODE'];
        feature.properties.name = feature.properties[admKey + '_NAME'] || '';

        if (!indexedValues[feature.id]) {
          indexedValues[feature.id] = {value: 0};
          self.palette.set({min: 0});
        }

        return _.extend(feature, {
          properties: _.extend(feature.properties, {
            value: indexedValues[feature.id].value
          })
        });
      })
    });

    this.set('geoJSON', geoJSON);
  }

});
