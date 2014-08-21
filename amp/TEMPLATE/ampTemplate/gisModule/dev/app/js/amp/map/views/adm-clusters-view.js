var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var ADMTemplate = fs.readFileSync(__dirname + '/../templates/map-adm-template.html', 'utf8');


module.exports = Backbone.View.extend({
  leafletLayerMap: {},

  admTemplate: _.template(ADMTemplate),

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;
    this.collection = this.app.admClusters;

    this.listenTo(this.app.data.admClusters, 'show', this.showLayer);
    this.listenTo(this.app.data.admClusters, 'hide', this.hideLayer);

  },

  render: function() {
    return this;
  },

  showLayer: function(admLayer) {
    var leafletLayer = this.leafletLayerMap[admLayer.cid];
    if (_.isUndefined(leafletLayer)) {
      leafletLayer = this.leafletLayerMap[admLayer.cid] = new L.layerGroup([]);
      admLayer.load().then(_.bind(function() {
        var clusters = this.getNewADMLayer(admLayer);
        leafletLayer.addLayer(clusters);
      }, this));
      this.listenToOnce(admLayer, 'processed', function() {
        var boundaries = this.getNewBoundary(admLayer);
        leafletLayer.addLayer(boundaries);
      });
    }

    this.map.addLayer(leafletLayer);

  },


  hideLayer: function(admLayer) {
    var leafletLayer = this.leafletLayerMap[admLayer.cid];
    if (_.isUndefined(leafletLayer)) {
      throw new Error('cannot remove a layer that is not loaded...', admLayer);
    }
    this.map.removeLayer(leafletLayer);
  },

  getNewADMLayer: function(admLayer) {
    var self = this;

    return new L.geoJson(admLayer.get('features'), {
      pointToLayer: function (feature, latlng) {
        var htmlString = self.admTemplate(feature);
        var myIcon = L.divIcon({
          className: 'map-adm-icon',
          html: htmlString,
          iconSize: [60, 50]
        });
        return L.marker(latlng, {icon: myIcon});//L.circleMarker(latlng, geojsonMarkerOptions);
      },
      onEachFeature: self._onEachFeature
    });
  },

  getNewBoundary: function(admLayer) {
    var boundaries = admLayer.get('boundary');
    if (boundaries) {
      return new L.geoJson(boundaries, {
        style: {
          weight: 1,
          dashArray: '3',
          fillColor: 'transparent'
        }
      });
    } else {
      console.error('no boundaries for admLayer?', admLayer);
    }
  },

  // Create pop-ups
  _onEachFeature: function(feature, layer) {
    if (feature.properties) {
      var activities = feature.properties.activityid;
      layer.bindPopup(feature.properties.admName + ' has ' + activities.length +' projects. <br>Graphs will go here.');
    }
  }

});
