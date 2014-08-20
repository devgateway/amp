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
    var self = this;
    var loadedLayer = this.leafletLayerMap[admLayer.cid];
    if (loadedLayer === 'loading') {
      console.warn('tried to show project clusters while they are still loading');
      return;
    } else if (typeof loadedLayer === 'undefined') {
      this.leafletLayerMap[admLayer.cid] = 'loading';  // will be replaced soon...
    }

    // fires when just the clusters have loaded
    admLayer.load().then(function() {
      self.leafletLayerMap[admLayer.cid] = self.getNewADMLayer(admLayer);
      self.map.addLayer(self.leafletLayerMap[admLayer.cid]);
    });

    // fires once boundary data has been joined. could do other joiney things
    this.listenToOnce(admLayer, 'processed', function() {
      self._loadBoundaryLayer(admLayer);
    });
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

    var clusters = new L.geoJson(admLayer.get('features'), {
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

    return new L.layerGroup([clusters]);
  },

  _loadBoundaryLayer: function(admLayer){    
    var boundaries = admLayer.get('boundary');
    if(boundaries){
      var boundaryLayer = new L.geoJson(boundaries, {
        style: {
          weight: 1,
          dashArray: '3',
          fillColor: 'transparent'
        }
      });
      if(! this.leafletLayerMap[admLayer.cid]){
        console.error('No layer yet...maybe a bad race condition... :(');
      } else {
        this.leafletLayerMap[admLayer.cid].addLayer(boundaryLayer);
      }
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
