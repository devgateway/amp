var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
var StructureClusterMixin = require('./structure-cluster-mixin');

var ProjectSiteTemplate = fs.readFileSync(__dirname + '/../templates/structure-template.html', 'utf8');


module.exports = Backbone.View
.extend(StructureClusterMixin).extend({
  // These will eventually move to a config.
  // They control when to resize points based on zoom
  // Make larger for smaller, denser countries: DRC = 7, Timor = 11
  ZOOM_BREAKPOINT: 11, //11 is real # for zoom resize
  SMALL_ICON_RADIUS: 4,
  BIG_ICON_RADIUS: 6,
  MAXCLUSTERRADIUS: 2,
  MAX_NUM_FOR_ICONS: 400,

  //    Calculate based on: var boundary0 = self.app.data.boundaries.get('adm-0');
  currentRadius: null,
  markerCluster: null,

  popup: null,
  structureTemplate: _.template(ProjectSiteTemplate),

  customClusterMap: {},
  maxClusterCount: 0,
  CLUSTER_PRECISION: 8, //decimal places of lat, lng precision for clustering. (doesn't effect plugin.)

  MAX_CLUSTER_SIZE: 20,

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;

    this.structureMenuModel = this.app.data.structuresMenu;

    this.initCluster();

    this.listenTo(this.structureMenuModel, 'show', this.showLayer);
    this.listenTo(this.structureMenuModel, 'hide', this.hideLayer);

    this.listenTo(this.app.data.structures, 'refresh', this.refreshLayer);
    //TODO: no need to send new request, just re-colour pallete..... maybe just try a hide and show...
    this.listenTo(this.structureMenuModel, 'change:filterVertical', this.refreshLayer);

    this.listenTo(this.markerCluster, 'clusterclick', this.clusterClick);

    _.bindAll(this, '_onEachFeature');
  },

  render: function() {
    return this;
  },


  // ==================
  // Point / Feature Code
  // ==================
  getNewProjectSitesLayer: function() {
    var self = this;
    // TODO: this approach will block structures drawing on join. Should draw dots as soon
    // as structures load, then update when activitites join is done...
    self.structureMenuModel.structuresCollection.getStructuresWithActivities().then(function() {
      self.rawData = self.structureMenuModel.structuresCollection.toGeoJSON();
      self._renderFeatures();
    });
    return this.featureGroup;
  },



  _renderFeatures: function() {
    var self = this;

    self.markerCluster.clearLayers();
    self.maxClusterCount = 0;
    self.customClusterMap = {};

    // add new featureGroup
    self.featureGroup = L.geoJson(self.rawData, {
      pointToLayer: function(feature, latlng) {
        var marker = null;

        if (self.rawData.features.length < self.MAX_NUM_FOR_ICONS &&
          self.structureMenuModel.get('filterVertical') === 'Primary Sector Id') {
          // create icon
          marker = self._createSectorMarker(latlng, feature);
        } else {
          // coloured circle marker, no icon
          marker = self._createPlainMarker(latlng, feature);
        }

        self.markerCluster.addLayer(marker);


        // DRS in progress custom own clustering. big efficiency gains.
        var latLngString = Math.round(latlng.lat * self.CLUSTER_PRECISION * 10) +
          ',' + Math.round(latlng.lng * self.CLUSTER_PRECISION * 10);
        if (self.customClusterMap[latLngString]) {
          self.customClusterMap[latLngString].push(marker); //TODO: should push marker or feature?
          self.maxClusterCount = Math.max(self.maxClusterCount, self.customClusterMap[latLngString].length);
        } else {
          self.customClusterMap[latLngString] = [marker];
        }

        return marker;
      },
      onEachFeature: self._onEachFeature
    });
  },

  // 1. SVG Icon: works well with agresive clustering: aprox 40 px range
  // or if < MAX_NUM_FOR_ICONS icons. Best on FF
  _createSectorMarker: function(latlng, feature) {
    var sectorCode = 0; // temp code for catchall...
    var filterVertical = this.structureMenuModel.get('filterVertical');

    if (feature.properties.activity.attributes &&
        feature.properties.activity.attributes.matchesFilters[filterVertical]) {
      if (feature.properties.activity.attributes.matchesFilters[filterVertical].length > 1) {
        console.log('TODO: need custom vairous sectors icon...different from  multi-sector');
      }
      sectorCode = feature.properties.activity.attributes.matchesFilters[filterVertical][0].get('code');
    }

    var pointIcon = L.icon({
      iconUrl: 'img/map-icons/' + this.structureMenuModel.iconMappings[sectorCode],
      iconSize:     [25, 25], // size of the icon
      iconAnchor:   [12, 25], // point of the icon which will correspond to marker's location
      popupAnchor:  [-3, -6] // point from which the popup should open relative to the iconAnchor
    });

    return L.marker(latlng, {icon: pointIcon});
  },

  // 0. origninaly way circle marker, no icon
  _createPlainMarker: function(latlng, feature) {

    var colors = this.structureMenuModel.structuresCollection.palette.colours.filter(function(colour) {
      return colour.get('test').call(colour, feature.properties.id);
    });
    if (colors.length > 2) {  // 2, because "other" is always true...
      colors = [this.structureMenuModel.structuresCollection.palette.colours.find(function(colour) {
        return colour.get('multiple') === true;
      })];
    }

    // temp hack for if pallette part didn't work.
    if (colors.length === 0) {
      colors = [{hex: function() { return 'orange';}}];
      //console.warn('colour not found for feature ', feature.properties.activity.attributes.matchesFilters);
    }

    // set radius based on zoom.
    if (this.map.getZoom() < this.ZOOM_BREAKPOINT) {
      this.currentRadius = this.SMALL_ICON_RADIUS;
    } else {
      this.currentRadius = this.BIG_ICON_RADIUS;
    }

    return new L.CircleMarker(latlng, {
      radius: this.currentRadius,
      fillColor: colors[0].hex(),
      color: null,
      weight: 1,
      opacity: 1,
      fillOpacity: 1
    });

  },

  // circles  shrink if we're zoomed out, get big if zoomed in
  _updateZoom: function() {
    var self = this;
    if (this.featureGroup) {
      var zoom = this.map.getZoom();
      // make small points
      if (zoom < this.ZOOM_BREAKPOINT && self.currentRadius !== self.SMALL_ICON_RADIUS) {
        self.currentRadius = self.SMALL_ICON_RADIUS;
        this.featureGroup.eachLayer(function(layer) {
          if (layer.setRadius) {
            layer.setRadius(self.currentRadius);
          }
        });
      } else if (zoom >= this.ZOOM_BREAKPOINT && self.currentRadius !== self.BIG_ICON_RADIUS) {
        self.currentRadius = self.BIG_ICON_RADIUS;
        this.featureGroup.eachLayer(function(layer) {
          if (layer.setRadius) {
            layer.setRadius(self.currentRadius);
          }
        });
      }
    }
  },

  _hilightProject: function(projectId) {
    this.featureGroup.eachLayer(function(layer) {
      if (layer.feature.properties.activity.id === projectId && layer.setStyle) {
        layer.setStyle({color: '#222', stroke: true});
      }
    });
  },

  _dehilightProject: function(projectId) {
    this.featureGroup.eachLayer(function(layer) {
      if (layer.feature.properties.activity.id === projectId && layer.setStyle) {
        layer.setStyle({stroke:false});
      }
    });
  },


  // ==================
  // Layer management
  // ==================
  showLayer: function(layer) {
    var self = this;

    if (this.layerLoadState === 'loading') {
      console.warn('ProjectSites leaflet: tried to show project sites while they are still loading');
      return;
    } else if (this.layerLoadState === 'pending') {
      this.layerLoadState = 'loading';
    }

    this.structureMenuModel.load().done(function() {
      if (layer.get('selected')) {
        self.layerLoadState = 'loaded';
        self.getNewProjectSitesLayer();
        self.map.addLayer(self.markerCluster);
      }
    });

    this.map.on('zoomend', this._updateZoom, this);

  },

  refreshLayer: function() {
    this.hideLayer();
    this.showLayer(this.structureMenuModel);
  },

  bringToFront: function() {
    if (this.markerCluster && this.markerCluster._featureGroup) {
      this.markerCluster._featureGroup.bringToFront();
    }
  },

  hideLayer: function() {
    if (this.layerLoadState === 'pending') {
      console.warn('Tried to remove project sites but they have not been added');
    } else if (this.layerLoadState === 'loading') {
      console.warn('Project Sites: removing layers while they are loading is not yet supported');
    }

    this.map.off('zoomend', this._updateZoom);
    this.map.removeLayer(this.markerCluster);
  }

});
