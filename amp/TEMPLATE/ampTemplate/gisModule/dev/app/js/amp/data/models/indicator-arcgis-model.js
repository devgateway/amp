var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var Palette = require('../../colours/colour-palette');


module.exports = Backbone.Model.extend({

  defaults: {
    min: Infinity,
    max: -Infinity
  },

  initialize: function() {
    // provide the promises API for a "load" method, even though we don't have to load anything
    this._esriLoaded = null;  // Deferred created in this.load
    this._esriProcessed = new Deferred();

    // TODO: factor this behavior into an indicator base class
    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });

    this.listenTo(this, 'change:min change:max', this.updatePaletteRange);

    _.bindAll(this, 'onEachFeature', 'styleNewFeature');

    this.palette = new Palette.FromRange({ seed: this.get('id') });
  },

  load: function() {
    var self = this;

    if (_.isNull(this._esriLoaded)) {
      this._esriLoaded = new Deferred();
      this.esriLayer = L.esri.featureLayer(this.get('link'), {
        simplifyFactor: 0.9,
        style: this.styleNewFeature,
        onEachFeature: this.onEachFeature
      }).on('load', this._esriLoaded.resolve);
      this._esriLoaded.resolve();
    }

    this._esriLoaded.then(function() {
      self.trigger('loaded processed', self);  // have to trigger processed here, because we can't
                                               // get any features until we add the layer to the map
    });

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
      color: colour.hex(),
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


