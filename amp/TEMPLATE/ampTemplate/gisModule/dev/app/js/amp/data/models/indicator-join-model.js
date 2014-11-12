var when = require('jquery').when;
var _ = require('underscore');
var Backbone = require('backbone');
var TopojsonLibrary = require('../../../libs/local/topojson.js');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Palette = require('../../colours/colour-palette');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({

  initialize: function() {
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });

    // TODO: do this later? Maybe set as a model attribute instead of directly
    // on the model? Should it sometimes be Discrete instead of FromRange?
    // sets base colour
    this.palette = new Palette.FromRange({ seed: this.get('id') });
  },

  loadBoundary: function() {
    // Phil's ideal way of being able to join with non-hosted boundaries.:
    // var boundaryLink = this.get('joinBoundariesLink');  // TODO: handle IDs vs links consitently
    // var boundaryId = boundaryLink.split('gis/boundaries/')[1];  // for now, (for ever?,) they are all local
    // var boundary = this.collection.boundaries.find(function(boundary) { return boundary.id === boundaryId; });

    var boundary = this.collection.boundaries.findWhere({id: this.get('value')});
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
    return when(this.load(), this.loadBoundary()).promise();
  },

  updatePaletteRange: function() {
    var min = +Infinity,
        max = -Infinity;

    _.each(this.get('values'), function(value) {
      if (value.amount < min) {
        min = value.amount;
      }
      if (value.amount > max) {
        max = value.amount;
      }
    });
    this.palette.set({min: min, max: max});
  },

  _joinDataWithBoundaries: function(boundaryGeoJSON) {
    var self = this;
    var indexedValues = _.indexBy(this.get('values'), 'admID');
    var admKey = this.id.replace('-', '').toUpperCase();
    // copy boundary geoJSON, and inject data
    var geoJSON = _.extend({}, boundaryGeoJSON, {
      features: _.map(boundaryGeoJSON.features, function(feature) {
        // replace boundary properties with {amount: value}
        // TODO... keep the existing properties and just add value?
        // replacing for now, to save weight
        feature.id = feature.properties[admKey + '_CODE'];
        feature.properties.name = feature.properties[admKey + '_NAME'] || '';

        if (!indexedValues[feature.id]) {
          indexedValues[feature.id] = {amount: 0};
          self.palette.set({min: 0});
        }

        return _.extend(feature, {
          properties: _.extend(feature.properties, {
            value: indexedValues[feature.id].amount
          })
        });
      })
    });
    this.set('geoJSON', geoJSON);
  }

});
