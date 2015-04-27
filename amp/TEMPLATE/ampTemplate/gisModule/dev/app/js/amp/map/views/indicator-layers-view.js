var $ = require('jquery');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
var util = require('../../../libs/local/chart-util');


module.exports = Backbone.View.extend({

  initialize: function(options) {
    var self = this;
    this.app = options.app;
    this.map = options.map;
    this.admClustersLayersView = options.admClustersLayersView;
    this.leafletLayerMap = {};

    $.when(this.app.data.filter.loaded, this.app.data._stateWait).then(function() {
      self.listenTo(self.app.data.indicators, 'show', self.showLayer);
      self.listenTo(self.app.data.indicators, 'hide', self.hideLayer);

      self.listenTo(self.app.data.hilightFundingCollection, 'show', self.refreshLayer);
      self.listenTo(self.app.data.hilightFundingCollection, 'hide', self.hideLayer);
      self.listenTo(self.app.data.hilightFundingCollection, 'sync', self.refreshLayer);
    });
  },

  // removes layer, then shows it. Important for layers whos content changes.
  refreshLayer: function(layer) {
    var loadedLayer = this.leafletLayerMap[layer.cid];

    if (loadedLayer) {
      this.map.removeLayer(loadedLayer);
    }
    this.showLayer(layer);
  },

  showLayer: function(layer) {
    var self = this,
        loadedLayer = this.leafletLayerMap[layer.cid];

    if (loadedLayer === 'loading') {
      console.warn('tried to show a lyer that is still loading, return');
      return;
    } else {
      //this will be replaced once loadAll is done,
      //prevent race condition 'show' calls adding multiple versions of the layer.
      this.leafletLayerMap[layer.cid] = 'loading';
    }
    layer.loadAll().done(function() {
      var layerType = layer.get('type');
      if (layerType === 'joinBoundaries') {  // geojson
        loadedLayer = self.getNewGeoJSONLayer(layer);
      } else if (layerType === 'wms') {
        loadedLayer = self.getNewWMSLayer(layer);
      } else if (layerType === 'arcgis' || layerType === 'Indicator Layers') {
        loadedLayer = self.getNewArcGISLayer(layer);
      } else {
        console.warn('Map view for layer type not implemented. layer:', layer);
      }
      self.leafletLayerMap[layer.cid] = loadedLayer;

      // only add it to the map if is still selected.
      if (layer.get('selected')) {
        self.map.addLayer(loadedLayer);
        if (loadedLayer.bringToBack) {
          loadedLayer.bringToBack();
          //TODO: drs, very dirty way of hiding boundaries so they don't hijack click events
          // I need to pull out boundaries into own view.
          self.admClustersLayersView.moveBoundaryBack();
        }
        self.trigger('addedToMap'); //TODO: better way. needed to let map bring structures to front.
      }
    });

    layer.load();
  },

  hideLayer: function(layer) {
    var leafletLayer = this.leafletLayerMap[layer.cid];
    if (leafletLayer) {
      this.map.removeLayer(leafletLayer);
    }
  },

  getNewGeoJSONLayer: function(layerModel) {
    var featureValue;
    var colour;
    var self = this;

    return new L.geoJson(layerModel.get('geoJSON'), {
      style: function(feature) {
        featureValue = feature.properties.value;
        // sets colour for each polygon
        colour = layerModel.palette.colours.find(function(colour) {
          return colour.get('test').call(colour, featureValue);
        });
        if (!colour) {

          colour = {hex: function() {return '#354';}};
          console.warn('No colour matched for the value ' + featureValue);
          //throw new Error('No colour matched for the value ' + featureValue);
        }
        return {
          color: colour.hex(),
          weight: 2,
          opacity: 0.9,
          fillOpacity: 0.6
        };
      },
      onEachFeature: function(feature, layer) {self.tmpFundingOnEachFeature(feature, layer, layerModel);}
    });
  },

  // used to hilight the geojson layer on click, show popup, and unhilight after.
  tmpFundingOnEachFeature: function(feature, layer, layerModel) {
    var self = this;
    // Add popup
    if (feature && feature.properties) {
      // TODO: drs append  format value.
      var unit = (layerModel.get('unit') ? layerModel.get('unit') : '');
      self.app.data.settings.load().then(function() {
        var fundingTypeString = '';
        if (self.app.data.settings.get('0')) {
          fundingTypeString = ['<strong>',
            self.app.data.settings.get('0').get('selectedName'),
            ': ',
            '</strong>'].join('');
        }
        var ampFormatter = new util.DecimalFormat(self.app.data.settings.get('number-format').get('name'));
        var fundingPopupTemplate = ['<strong>', feature.properties.name, '</strong>',
                        '<br/>', fundingTypeString, '',
                        ampFormatter.format(feature.properties.value), ' ', unit].join('');

        layer.bindPopup(fundingPopupTemplate);
      });
    }

    // hilight and unhilight the area when a user clicks on them..
    layer.on('popupopen', function() {
      layer.setStyle({
        fillOpacity: 1.0
      });
    });
    layer.on('popupclose', function() {
      layer.setStyle({
        fillOpacity: 0.6
      });
    });
  },

  getNewWMSLayer: function(layer) {
    return L.tileLayer.wms(layer.get('link'), {
      layers: layer.get('layer'),
      // TODO: should these details be obtained from the API?
      format: 'image/png',
      transparent: true,
      opacity: 0.75
    });
  },

  getNewArcGISLayer: function(layer) {
    return layer.esriLayer;
  }

});
