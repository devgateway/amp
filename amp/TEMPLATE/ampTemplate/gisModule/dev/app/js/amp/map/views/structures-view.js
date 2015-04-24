var $ = require('jquery');
var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
var StructureClusterMixin = require('./structure-cluster-mixin');

var ProjectSiteTemplate = fs.readFileSync(__dirname + '/../templates/structure-template.html', 'utf8');


function breathAfter(func, context) {
  return function(/* arguments */) {
    var finished = new $.Deferred(),
        result = func.apply(context, arguments);
    window.setTimeout(function() {
      finished.resolve(result);
    }, 50);
    return finished.promise();
  };
}


module.exports = Backbone.View
.extend(StructureClusterMixin).extend({
  // These will eventually move to a config.
  // They control when to resize points based on zoom
  // Make larger for smaller, denser countries: DRC = 7, Timor = 11
  ZOOM_BREAKPOINT: 11, //11 is real # for zoom resize
  SMALL_ICON_RADIUS: 4,
  BIG_ICON_RADIUS: 6,
  MAX_NUM_FOR_ICONS: -1,

  // Calculate based on: var boundary0 = self.app.data.boundaries.get('adm-0');
  currentRadius: null,
  markerCluster: null,

  popup: null,
  structureTemplate: _.template(ProjectSiteTemplate),

  customClusterMap: {},
  maxClusterCount: 4, //start it at 4 instead of 0, otherwise clusters of '2' can be way too big.
  CLUSTER_PRECISION: 2, //decimal places of lat, lng precision for clustering. (doesn't effect plugin.)

  MAX_CLUSTER_SIZE: 20,

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;

    this.structureMenuModel = this.app.data.structuresMenu;

    this.initCluster();

    this.listenTo(this.structureMenuModel, 'show', this.showLayer);
    this.listenTo(this.structureMenuModel, 'hide', this.hideLayer);

    this.listenTo(this.app.data.structures, 'refresh', this.refreshLayer);

    this.listenTo(this.markerCluster, 'clusterclick', this.clusterClick);

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
    // Phil note: I'm not sure if the extra complexity will be worth it. We can make joins fast,
    //   it's the clustering and just drawing dots on the map that is hard to optimize.
    self.structureMenuModel.structuresCollection.getStructuresWithActivities()
      .then(function() {  // 160ms on Phil's computer
        self.rawData = self.structureMenuModel.structuresCollection.toGeoJSON();
        self._renderFeatures()
          .then(function() {
            self.map.addLayer(self.markerCluster);  // maybe TODO: chunk (takes 271ms on Phil's computer)
          });
      });

    return this.featureGroup;
  },



  _renderFeatures: function() {
    var self = this;
    self.markerCluster.clearLayers();
    self.maxClusterCount = 4; //start it at 4 instead of 0, otherwise clusters of 2 or 3 can be way too big.
    self.customClusterMap = {};

    // add new featureGroup
    self.featureGroup = L.layerGroup();

    return $.Deferred().resolve()  // kick things off, so we can async this sequence with breathAfter
      .then(breathAfter(function() {  // wait 50ms, then:
        return _(self.rawData.features)
          .map(_(self._featureToMarker).bind(self));
      }))
      .then(function(markers) {
        self.markerCluster.addLayers(markers);
        _(markers).each(function(marker) {
          self.featureGroup.addLayer(marker);
        });
      });
  },


  _featureToMarker: function(feature) {  // 152ms on Phil's computer
    var self = this,
        marker,
        latlng = L.latLng(feature.geometry.coordinates[1],
                          feature.geometry.coordinates[0]);

    // Calculate only one time and not for all points (we can have thousands).
    if (self.MAX_NUM_FOR_ICONS === -1) {
    	//TODO: Move this code to a config class.
	    var useIconsForSectors = _.find(app.data.settings.models, function(item) {
	  	  	return (item.id === 'use-icons-for-sectors-in-project-list');
	    });
	    var maxLocationIcons = _.find(app.data.settings.models, function(item) {
	    	return (item.id === 'max-locations-icons');
		});
	    if (useIconsForSectors !== undefined && useIconsForSectors.get('name') === 'true') {
	    	if (maxLocationIcons !== undefined && maxLocationIcons.get('name') !== "" && maxLocationIcons.get('name') !== "0") {
	    		self.MAX_NUM_FOR_ICONS = parseInt(maxLocationIcons.get('name'));
	    	} else {
	    		self.MAX_NUM_FOR_ICONS = 0;
	    	}
	    } else {
	    	self.MAX_NUM_FOR_ICONS = 0;
	    }
	    console.log('MAX_NUM_FOR_ICONS: ' + self.MAX_NUM_FOR_ICONS);
    }
    if (self.rawData.features.length < self.MAX_NUM_FOR_ICONS &&
      self.structureMenuModel.get('filterVertical') === 'Primary Sector Id') {
      // create icon
      marker = self._createSectorMarker(latlng, feature);
    } else {
      // coloured circle marker, no icon
      marker = self._createPlainMarker(latlng, feature);
    }

    marker.feature = feature;  // L.geoJSON would do this implicitely
                               // so add it manually to keep the same API

    // self.markerCluster.addLayer(marker);

    // DRS in progress custom own clustering. big efficiency gains.
    var latLngString = Math.round(latlng.lat * Math.pow(10, self.CLUSTER_PRECISION)) +
      ',' + Math.round(latlng.lng * Math.pow(10, self.CLUSTER_PRECISION));
    if (self.customClusterMap[latLngString]) {
      self.customClusterMap[latLngString].push(marker); //TODO: should push marker or feature?
      self.maxClusterCount = Math.max(self.maxClusterCount, self.customClusterMap[latLngString].length);
    } else {
      self.customClusterMap[latLngString] = [marker];
    }

    self._bindPopup(marker);

    return marker;
  },


  // 1. SVG Icon: works well with agresive clustering: aprox 40 px range
  // or if < MAX_NUM_FOR_ICONS icons. Best on FF
  _createSectorMarker: function(latlng, feature) {
    var sectorCode = 0; // temp code for catchall...
    var filterVertical = this.structureMenuModel.get('filterVertical');
//feature.properties.activity.attributes.matchesFilters[filterVertical]
    if (feature.properties.activity.attributes &&
      	_.has(feature.properties.activity.attributes.matchesFilters, filterVertical)) {
      if (feature.properties.activity.attributes.matchesFilters[filterVertical] == null) {
    	  //It has no sector/donor
    	  sectorCode = '1';
      } else if (feature.properties.activity.attributes.matchesFilters[filterVertical].length > 1) {
       sectorCode = '0';
       console.warn('TODO: need custom vairous sectors icon...different from  multi-sector');
      } else {
       if (feature.properties.activity.attributes.matchesFilters[filterVertical][0] instanceof Object) {
    	  sectorCode = feature.properties.activity.attributes.matchesFilters[filterVertical][0].get('code');
       }
       else {
    	   sectorCode = feature.properties.activity.attributes.matchesFilters[filterVertical][0];
       }
      }
    }
    var pointIcon = L.icon({
      iconUrl: 'img/map-icons/' + this.structureMenuModel.iconMappings[sectorCode],
      iconSize:     [25, 25], // size of the icon
      iconAnchor:   [12, 25], // point of the icon which will correspond to marker's location
      popupAnchor:  [-3, -6]  // point from which the popup should open relative to the iconAnchor
    });

    return L.marker(latlng, {icon: pointIcon});
  },

  // 0. origninaly way circle marker, no icon
  _createPlainMarker: function(latlng, feature) {  // 122ms on Phil's computer

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

  // Create pop-ups
  _bindPopup: function(marker) {
    var self = this,
        feature = marker.feature;

    /* TODO(thadk) switch individual feature to this standard parsed model input*/
    /*var parsedProjectSitesList = this.app.data.structures.model.prototype.parse(feature);*/

    if (feature.properties) {
      var activityJSON = feature.properties.activity.toJSON();
      marker.bindPopup(self.structureTemplate({
        activityJSON: activityJSON,
        properties: feature.properties
      }),
      {
        maxWidth: 450,
        offset: new L.Point(0, -2)
      });
    }

    marker.on('click', function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.activity.id;
        self._hilightProject(projectId);
      }
      app.translator.translateDOM($('.cluster-popup'));
    });

    marker.on('popupclose', function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.activity.id;
        self._dehilightProject(projectId);
      }
    });
  },

  _hilightProject: function(projectId) {
    this.featureGroup.eachLayer(function(layer) {
      if (layer.feature.properties.activity.id === projectId && layer.setStyle) {
        layer.setStyle({color: '#222', stroke: true, weight:2});
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
      }
    });

    this.map.on('zoomend', this._updateZoom, this);

  },

  refreshLayer: function() {
    // TODO: this is getting called twice when showing sturctures
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
