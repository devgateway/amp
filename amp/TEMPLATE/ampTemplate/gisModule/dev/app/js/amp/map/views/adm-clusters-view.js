/* global app */
var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var TopojsonLibrary = require('../../../libs/local/topojson.js');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var ADMTemplate = fs.readFileSync(__dirname + '/../templates/map-adm-template.html', 'utf8');

var ClusterPopupView = require('../views/cluster-popup-view');

module.exports = Backbone.View.extend({
  leafletLayerMap: {},

  admTemplate: _.template(ADMTemplate),

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;
    this.collection = this.app.admClusters;

    this.listenTo(this.app.data.admClusters, 'show', this.showLayer);
    this.listenTo(this.app.data.admClusters, 'hide', this.hideLayer);
    this.listenTo(this.app.data.admClusters, 'sync', this.refreshLayer);

  },

  render: function() {
    app.translator.translateDOM($('.cluster-popup'));
    return this;
  },


  showLayer: function(admLayer) {
	var self = this;
    var leafletLayerGroup = this.leafletLayerMap[admLayer.cid];

    // if it's not loaded yet, we need to load it.
    if (_.isUndefined(leafletLayerGroup)) {
      leafletLayerGroup = this.leafletLayerMap[admLayer.cid] = new L.layerGroup([]);
      admLayer.load().then(_.bind(function() {
        self._createClusters(admLayer, leafletLayerGroup);
      }, this));
      admLayer.loadAll().done(function() {
        self.boundary = self.getNewBoundary(admLayer);
        leafletLayerGroup.addLayer(self.boundary);
        self.moveBoundaryBack();
        $('#map-loading').hide();
      });
    } else {
      // else layer is already loaded.
      $('#map-loading').hide();
    }

    if (admLayer.get('selected')) {
      this.map.addLayer(leafletLayerGroup);
    }
  },

  //TODO: make sure still selected
  refreshLayer: function(admLayer) {
    var self = this;
    var leafletLayerGroup = this.leafletLayerMap[admLayer.cid];
    if (leafletLayerGroup) {
      leafletLayerGroup.clearLayers();
      self._createClusters(admLayer, leafletLayerGroup);

      this.boundary = self.getNewBoundary(admLayer);
      if (this.boundary) {
        leafletLayerGroup.addLayer(this.boundary);
        this.moveBoundaryBack();
        $('#map-loading').hide();
      }

    } else {
      // don't listen to event if admLayer is actually the entire collection.
      if (!admLayer.models) {
        this.showLayer(admLayer);
      }
    }
  },

  moveBoundaryBack: function() {
    if (this.boundary && this.boundary._map) {
      this.boundary.bringToBack();
    }
  },

  _createClusters: function(admLayer, leafletLayerGroup) {
    var self = this;
    var clusters = this.getNewADMLayer(admLayer);
    leafletLayerGroup.addLayer(clusters);
    clusters.on('popupopen', function(e) {
      var clusterPopupView = new ClusterPopupView({app: self.app}, e.popup, admLayer);
      clusterPopupView.render();
    });
  },

  hideLayer: function(admLayer) {
    var leafletLayerGroup = this.leafletLayerMap[admLayer.cid];
    if (leafletLayerGroup) {
      this.map.removeLayer(leafletLayerGroup);
    }
  },

  getNewADMLayer: function(admLayer) {
    var self = this;

    return new L.geoJson(admLayer.get('features'), {
      pointToLayer: function(feature, latlng) {
        var htmlString = self.admTemplate(feature);
        var myIcon = L.divIcon({
          className: 'map-adm-icon',
          html: htmlString,
          iconSize: [60, 50]
        });
        return L.marker(latlng, {icon: myIcon});//L.circleMarker(latlng, geojsonMarkerOptions);
      },
      onEachFeature: function (feature, layer) {    	  
    	  self._onEachFeature(feature, layer, admLayer);
      }
    	  
    });
  },

  // TODO: currently reparses topojson everytime settings/filters change...
  // instead it should do it once and cache it in a variable like boundaryLayerMap
  getNewBoundary: function(admLayer) {
    var topoboundaries = admLayer.get('boundary');

    if (topoboundaries) {
      /* retrieve the TopoJSON index key */
      var topoJsonObjectsIndex = _.chain(topoboundaries.objects)
                               .keys()
                               .first()
                               .value();
      var boundaries = TopojsonLibrary.feature(topoboundaries, topoboundaries.objects[topoJsonObjectsIndex]);

      if (boundaries) {
        return new L.geoJson(boundaries, {
          style: {
            weight: 1,
            dashArray: '3',
            color: '#243241',
            fillColor: 'transparent'
          }
        });
      } else {
        console.error('no boundaries for admLayer?', admLayer);
      }
    }
  },


  // Create pop-ups
  _onEachFeature: function(feature, layer, admLayer) {
    if (feature.properties) {
      var activities = feature.properties.activityid;
      layer._clusterId = feature.properties.admName;
      feature.properties.admLevel = admLayer.get('title');
      // temp. will be template.
      layer.bindPopup(feature.properties.admName +
        ' has ' +  activities.length +
        ' projects. <br><img src="img/loading-icon.gif" />',
        {maxWidth: 500, offset: new L.Point(0, -16)}
        );
    }
  }
});
