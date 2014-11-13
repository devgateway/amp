var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var ProjectListTemplate = fs.readFileSync(__dirname + '/../templates/structure-popup-template.html', 'utf8');

module.exports = Backbone.View.extend({
  // These will eventually move to a config.
  // They control when to resize points based on zoom
  // Make larger for smaller, denser countries: DRC = 7, Timor = 11
  ZOOM_BREAKPOINT: 11,
  SMALL_ICON_RADIUS: 4,
  BIG_ICON_RADIUS: 6,
  //    Calculate based on: var boundary0 = self.app.data.boundaries.get('adm-0');
  currentRadius: null,
  markerCluster: null,

  popup: null,
  projectListTemplate: _.template(ProjectListTemplate),

  customClusterMap: {},
  maxClusterCount: 0,
  CLUSTER_PRECISION: 8, //decimal places of lat, lng precision for clustering. (doesn't effect plugin.)

  MAX_CLUSTER_SIZE: 30,

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;
    this.structureMenuModel = this.app.data.projectSitesMenu;

    this.initCluster();

    this.listenTo(this.structureMenuModel, 'show', this.showLayer);
    this.listenTo(this.structureMenuModel, 'hide', this.hideLayer);

    this.listenTo(this.app.data.projectSites, 'sync', this.refreshLayer); //TODO: implement refresh layer

    this.listenTo(this.markerCluster, 'clusterclick', this.clusterClick);

    _.bindAll(this, '_onEachFeature');
  },

  render: function() {
    return this;
  },


  // ==================
  // Point / Feature Code
  // ==================
  getNewProjectSitesLayer: function(structureMenuModel) {
    var self = this;
    // TODO: this approach will block structures drawing on join. Should draw dots as soon
    // as structures load, then update when activitites join is done...
    structureMenuModel.structuresCollection.getStructuresWithActivities().then(function() {
      self.rawData = structureMenuModel.structuresCollection.toGeoJSON();
      self._renderFeatures();
    });
    return this.featureGroup;
  },

  //TODO: this code runs every time layer is shown...should only do it when things change,
  // otherwise should just hide and show.
  _renderFeatures: function() {
    var self = this;

    self.markerCluster.clearLayers();
    self.maxClusterCount = 0;
    self.customClusterMap = {};

    // add new featureGroup
    self.featureGroup = L.geoJson(self.rawData, {
      pointToLayer: function(feature, latlng) {
        var colors = self.structureMenuModel.structuresCollection.palette.colours.filter(function(colour) {
          var donorId = -1;
          if (feature.properties.activity.attributes.matchesFilters['Donor Id']) {
            donorId = feature.properties.activity.attributes.matchesFilters['Donor Id'][0];
          }

          return colour.get('test').call(colour, feature.properties.id);
        });
        if (colors.length > 2) {  // 2, because "other" is always true...
          colors = [self.structureMenuModel.structuresCollection.palette.colours.find(function(colour) {
            return colour.get('multiple') === true;
          })];
        }

        // temp hack for if pallette part didn't work.
        if (colors.length === 0) {
          colors = [{hex: function() { return 'orange';}}];
          console.warn('colour not found');
        }

        if (self.map.getZoom() < self.ZOOM_BREAKPOINT) {
          self.currentRadius = self.SMALL_ICON_RADIUS;
        } else {
          self.currentRadius = self.BIG_ICON_RADIUS;
        }

        var point = new L.CircleMarker(latlng, {
          radius: self.currentRadius,
          fillColor: colors[0].hex(),
          color: null,
          weight: 1,
          opacity: 1,
          fillOpacity: 1
        });

        self.markerCluster.addLayer(point);

        // DRS in progress custom own clustering. big efficiency gains.
        var latLngString = Math.round(latlng.lat * self.CLUSTER_PRECISION * 10) +
          ',' + Math.round(latlng.lng * self.CLUSTER_PRECISION * 10);
        if (self.customClusterMap[latLngString]) {
          self.customClusterMap[latLngString].push(point); //TODO: should push point or feature?
          self.maxClusterCount = Math.max(self.maxClusterCount, self.customClusterMap[latLngString].length);
        } else {
          self.customClusterMap[latLngString] = [point];
        }

        return point;
      },
      onEachFeature: self._onEachFeature
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
          layer.setRadius(self.currentRadius);
        });
      } else if (zoom >= this.ZOOM_BREAKPOINT && self.currentRadius !== self.BIG_ICON_RADIUS) {
        self.currentRadius = self.BIG_ICON_RADIUS;
        this.featureGroup.eachLayer(function(layer) {
          layer.setRadius(self.currentRadius);
        });
      }
    }
  },

  _hilightProject: function(projectId) {
    this.featureGroup.eachLayer(function(layer) {
      var properties = layer.feature.properties;
      if (properties.projectId === projectId) {
        layer.setStyle({color: '#222', stroke: true});
      }
    });
  },

  _dehilightProject: function(projectId) {
    this.featureGroup.eachLayer(function(layer) {
      var properties = layer.feature.properties;
      if (properties.projectId === projectId) {
        layer.setStyle({stroke:false});
      }
    });
  },


  // ==================
  // Cluster Code
  // ==================
  initCluster: function() {
    var self = this;

    this.layerLoadState = 'pending';  // 'loading', 'loaded'.

    var model = self.app.data.projectSites;

    //TODO: checkout prune cluster, supposedly way faster...
    // may also be worth doing manually since we don't want updates on zoom
    // TODO: make sizing dynamic based on highest cluster... and put into own function...
    this.markerCluster = new L.markerClusterGroup({
      maxClusterRadius: 0.9,
      iconCreateFunction: function(cluster) {return self._createCluster(cluster, model);},
      zoomToBoundsOnClick: false,
      showCoverageOnHover: false,
      spiderfyOnMaxZoom: false,
      removeOutsideVisibleBounds: true
    });
  },

  _createCluster: function(cluster, model) {
    var self = this;
    var markers = cluster.getAllChildMarkers();
    var size = (markers.length / self.maxClusterCount) * self.MAX_CLUSTER_SIZE;
    size = Math.min(self.MAX_CLUSTER_SIZE, size);
    size = Math.max(self.BIG_ICON_RADIUS, size);

    var zoomedIn = (self.map.getZoom() >= self.ZOOM_BREAKPOINT);

    if (zoomedIn) {
      size += self.BIG_ICON_RADIUS;
    }

    var colors = _(markers)
      .chain()
      .map(function(m) {
        //self.structureMenuModel.structuresCollection
        var colour = model.palette.colours.find(function(c) {
          return c.get('test').call(c, (m.feature ? m.feature.properties.id : -1));
        });
        return colour;
      })
      .uniq()
      .value();

    if (colors.length > 1) {
      colors = [model.palette.colours.find(function(c) {
        return c.get('multiple') === true;
      })];
    }

    //var colors = [{hex: function() { return 'orange';}}];

    var marker = new L.circleDivIcon(size, {
        className: 'marker-cluster' + (zoomedIn ? '' : ' marker-cluster-small'),
        html: (zoomedIn ? '<div class="text">' + markers.length + '</div>' : ''),
        color: '#444',
        fillColor: (colors[0] && colors[0].hex()),
        weight: 1
      });

    return marker;
  },

  clusterClick: function(a) {
    var self = this;
    //TODO: seems silly to bind on every click...
    // Once we have nailed down desired functionality it will be worth writing our own clusterer since it only
    // needs to run once on project site load and that is it. (not each zoom etc.)
    var parsedProjectSitesList = _.chain(a.layer.getAllChildMarkers())
      .pluck('feature')
      .map(this.app.data.projectSites.model.prototype.parse)
      .value();


    /*
     *var childMarkerFeatureList = _.chain(a.layer.getAllChildMarkers())
     *   .pluck('feature')
     *   .pluck('properties')
     *   .value();
     */

    /* NB: Structures are a synonym of Project Sites. */
    a.layer.bindPopup(
      self.projectListTemplate({ structures: parsedProjectSitesList}),
      {
        maxWidth: 500,
        offset: new L.Point(0, -6)
      });

    a.layer.openPopup(self.map);
  },


  // Create pop-ups
  _onEachFeature: function(feature, layer) {
    var self = this;

    /* TODO(thadk) switch individual feature to this standard parsed model input*/
    /*var parsedProjectSitesList = this.app.data.projectSites.model.prototype.parse(feature);*/

    if (feature.properties) {
      //TODO: template:
      layer.bindPopup('Project #: ' + (feature.properties.activity ? feature.properties.activity[0] : '') +
        '<br />Site: ' + feature.properties.title +
        '<br />Description: ' + feature.properties.description,
      {
        maxWidth: 500,
        offset: new L.Point(0, -2)
      });
    }

    layer.on('click', function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.projectId;
        self._hilightProject(projectId);
      }
    });

    layer.on('popupclose', function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.projectId;
        self._dehilightProject(projectId);
      }
    });
  },

  // ==================
  // Layer management
  // ==================
  showLayer: function(projectSitesMenuModel) {
    var self = this;
    if (this.layerLoadState === 'loading') {
      console.warn('ProjectSites leaflet: tried to show project sites while they are still loading');
      return;
    } else if (this.layerLoadState === 'pending') {
      this.layerLoadState = 'loading';
    }

    projectSitesMenuModel.loadAll().done(function() {
      self.layerLoadState = 'loaded';
      self.getNewProjectSitesLayer(projectSitesMenuModel);
      self.map.addLayer(self.markerCluster);
    });

    this.map.on('zoomend', this._updateZoom, this);

  },

  refreshLayer: function() {
    console.log('TODO: refresh project site layer.');
  },

  bringToFront: function() {
    if (this.markerCluster && this.markerCluster._featureGroup) {
      this.markerCluster._featureGroup.bringToFront();
    }
  },

  hideLayer: function() {
    if (this.layerLoadState === 'pending') {
      throw new Error('Tried to remove project sites but they have not been added');
    } else if (this.layerLoadState === 'loading') {
      console.warn('Project Sites: removing layers while they are loading is not yet supported');
    }

    this.map.off('zoomend', this._updateZoom);
    this.map.removeLayer(this.markerCluster);
  }

});
