var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
// var Colors = require('../collections/layer-colors-collection');

module.exports = Backbone.View.extend({

  initialize: function(extraProperties) {
    _.extend(this, extraProperties);
    this.visible = false;

    this.listenTo(Backbone, 'MAP_LOAD_INDICATOR', this._loadIndicatorLayer);
  },

  render: function() {
    return this;
  },

  _loadIndicatorLayer: function(indicator) {
    // remove current layer if its there:
    if (this.indicatorLayer) {
      this.map.removeLayer(this.indicatorLayer);
      this.visible = false;
    }

    // this is currently how this layer is removed (which is done, in the prev if)
    if (indicator === null) {
      return;
    }

    if (indicator) {
      this.visible = true;
      if (indicator.get('type') === 'arcgis') {
        this._loadArcGISLayer(indicator);
      } else if (indicator.get('type') === 'geoJSON') {
        this._loadGeoJSONLayer(indicator);
      } else {
        console.error('Could not load layer for this indicator type:', indicator);
      }
    }
  },

  // TODO: need tof ind some way of doing equivalent to geoJSON .bringToBack();
  _loadArcGISLayer: function(indicator) {
    var self = this;
    var property = indicator.get('property');
    var maxValue = 0;

    this.indicatorLayer = L.esri.Layers.featureLayer(indicator.get('featurePath'),{
      simplifyFactor: 0.9,
      style:  function (feature) {
          maxValue = Math.max(maxValue, feature.properties[property]);
          return {color: 'blue',  weight: 1, opacity:0.4 };
        },
    }).addTo(self.map);

    // load is triggered after every pan, because on pan it asks for more feature info from server.
    // set all styles after they are on page and we know the max
    this.listenTo(this.indicatorLayer, 'load', function() {
      self._updateESRIIndicatorStyles(maxValue);
    });
    this.listenToOnce(this.indicatorLayer, 'load', function(e) {
      // this.map.fitBounds(e.bounds);  // not even once...
      this.map.fitBounds([[-90, -90], [90, 90]]);
    });
  },

  _loadGeoJSONLayer: function(indicator) {
    var self = this;
    var property = indicator.get('property');
    var maxValue = 0;

    //TODO: may need to apply API base url to begining of path
    $.get(indicator.get('featurePath')).then(function(geoJsonData) {

      // create layer
      self.indicatorLayer = L.geoJson(
        geoJsonData, {
        style:  function (feature) {
            maxValue = Math.max(maxValue, feature.properties[property]);
            return {color: 'blue',  weight: 1,  opacity:0.4, fillOpacity:0.5 };
          }
      }).addTo(self.map).bringToBack();
      self._updateIndicatorStyles(maxValue, property);

      self.map.fitBounds(self.indicatorLayer.getBounds());
    });

  },

  // for esri indicator groups..
  _updateESRIIndicatorStyles: function(maxValue, property) {
    var self = this;

    this.indicatorLayer.eachFeature(function(layer){
      var id = layer.feature.id;
      var featurePercent = layer.feature.properties[property] * 100 / maxValue;
      // self.indicatorLayer.setFeatureStyle(id, {color: rgb, fillColor:rgb, weight: 1, fillOpacity:0.5 });
    });

    // this.map.fitBounds(bounds);  // lol.. does not work...
  },

  // For normal Leaflet feature group
  _updateIndicatorStyles: function(maxValue, property) {

    // this.indicatorLayer.setStyle(function(feature) {
    //   var featurePercent = feature.properties[property] * 100 / maxValue;
    //   return {color: rgb, fillColor: rgb, weight: 1, fillOpacity:0.6 };
    // });
  },

});
