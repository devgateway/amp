var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var Palette = require('../../colours/colour-palette');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({

  defaults: {
    min: Infinity,
    max: -Infinity
  },

  initialize: function() {
    this.palette = new Palette.FromRange({ seed: this.get('id') });
    _.bindAll(this, 'onEachFeature', 'styleNewFeature');
    this.listenTo(this, 'change:min change:max', this.updatePaletteRange);

    // TODO: factor this behavior into an indicator base class
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });
  },

  fetch: function() {
    if (!_(this).has('_esriLoaded')) {
      this._esriLoaded = new Deferred();
      this.esriLayer = new L.esri.featureLayer(this.get('link'), {
        simplifyFactor: 0.9,
        style: this.styleNewFeature,
        onEachFeature: this.onEachFeature
      });
      this._esriLoaded.resolveWith(this, [this, this.esriLayer]);
    }
    return this._esriLoaded.promise();
  },

  onEachFeature: function(feature) {
    var val = feature.properties[this.get('property')];
    if (val < this.get('min')) {
      this.set('min', val);
    }
    if (val > this.get('max')) {
      this.set('max', val);
    }
  },

  _valueStyleObj: function(value) {
    var colour = this.palette.colours.find(function(colour) {
      return colour.get('test')(value);
    });
    return {
      color: colour && colour.hex(),
      weight: 4,
      opacity: 0.9,
      fillOpacity: 0.6
    };
  },

  styleNewFeature: function(feature) {
    // var id = feature.id;
    var value = feature.properties[this.get('property')];
    return this._valueStyleObj(value);
  },

  updatePaletteRange: function() {
    this.palette.set({ min: this.get('min'), max: this.get('max') });
    this.esriLayer.eachFeature(function(feature) {
      var id = feature.feature.id,
          value = feature.feature.properties[this.get('property')];
      this.esriLayer.setFeatureStyle(id, this._valueStyleObj(value));
    }, this);
  }

});


