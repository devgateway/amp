var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var ProjectListTemplate = fs.readFileSync(__dirname + '/../templates/structure-list-template.html', 'utf8');


module.exports = Backbone.View.extend({
  // These will eventually move to a config.
  // They control when to resize points based on zoom
  ZOOM_BREAKPOINT: 7,
  SMALL_ICON_RADIUS: 2,
  BIG_ICON_RADIUS: 7,
  currentRadius: 2,
  markerCluster: null,

  popup: null,
  projectListTemplate: _.template(ProjectListTemplate),

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;

    this.initCluster();

    this.listenTo(this.app.data.projectSites, 'show', this.showLayer);
    this.listenTo(this.app.data.projectSites, 'hide', this.hideLayer);

    this.listenTo(this.markerCluster, 'clusterclick', this.clusterClick);

    _.bindAll(this, '_onEachFeature');
  },

  initCluster: function() {
    var self = this;

    this.layerLoadState = 'pending';  // 'loading', 'loaded'.

    var model = self.app.data.projectSites.at(0);

    //TODO: checkout prune cluster, supposedly way faster...
    // may also be worth doing manually since we don't want updates on zoom
    // TODO: make sizing dynamic based on highest cluster... and put into own function...
    this.markerCluster = new L.markerClusterGroup({
      maxClusterRadius: 0.001,
      iconCreateFunction: function(cluster) {
        var markers = cluster.getAllChildMarkers();
        var size = markers.length + 5,
            zoomedIn = (self.map.getZoom() >= self.ZOOM_BREAKPOINT);

        if (zoomedIn) {
          size += 2 + self.BIG_ICON_RADIUS;
        }

        var colours = _(markers)
          .chain()
          .map(function(m) {
            var colour = model.palette.colours.find(function(c) {
              return c.get('test').call(c, m.feature.id);
            });
            return colour;
          })
          .uniq()
          .value();

        if (colours.length > 1) {
          colours = [model.palette.colours.find(function(c) {
            return c.get('multiple') === true;
          })];
        }

        var marker = new L.divIcon({
          className: 'marker-cluster' + (zoomedIn ? '' : ' marker-cluster-small'),
          iconSize: L.point(size, size),
          html: '<div class="circle" style="background-color:' + (colours[0] && colours[0].hex()) + '">'+
                  (zoomedIn ? '<div class="text">' + markers.length + '</div>' : '') +
                '</div>'
        });

        return marker;
      },
      zoomToBoundsOnClick: false,
      showCoverageOnHover: false,
      spiderfyOnMaxZoom: false,
      removeOutsideVisibleBounds: true
    });
  },

  showLayer: function(projectSitesModel) {
    if (this.layerLoadState === 'loading') {
      console.warn('tried to show project sites while they are still loading');
      return;
    } else if (this.layerLoadState === 'pending') {
      this.layerLoadState = 'loading';
    }

    this.listenToOnce(projectSitesModel, 'processed', function() {
      this.layerLoadState = 'loaded';
      this.getNewProjectSitesLayer(projectSitesModel);
      this.map.addLayer(this.markerCluster);
    });

    this.map.on('zoomend', this._updateZoom, this);

    projectSitesModel.load();
  },

  bringToFront: function(){
    if(this.markerCluster && this.markerCluster._featureGroup){
      this.markerCluster._featureGroup.bringToFront();
    }
  },

  hideLayer: function() {
    if (this.layerLoadState === 'pending') {
      throw new Error('Tried to remove project sites but they have not been added');
    } else if (this.layerLoadState === 'loading') {
      console.warn('removing layers while they are loading is not yet supported');
    }

    this.map.off('zoomend', this._updateZoom);

    this.map.removeLayer(this.markerCluster);
  },


  getNewProjectSitesLayer: function(projectSitesModel) {
    this.features = projectSitesModel.get('features');
    this.rawData = projectSitesModel.attributes;
    this._renderFeatures();
    return this.featureGroup;
  },


  render: function() {
    return this;
  },

  clusterClick: function(a) {
    var self = this;
    //TODO: seems silly to bind on every click...
    a.layer.bindPopup(self.projectListTemplate({ projects: a.layer.getAllChildMarkers() }));
    a.layer.openPopup(self.map);
  },


  _renderFeatures: function() {
    var self = this;

    self.markerCluster.clearLayers();

    var model = self.app.data.projectSites.at(0);

    // add new featureGroup
    //console.log(JSON.stringify(self.rawData));
    self.featureGroup = L.geoJson(self.rawData, {
      pointToLayer: function (feature, latlng) {
        var colors = model.palette.colours.filter(function(colour) {
          return colour.get('test').call(colour, feature.id);
        });
        if (colors.length > 2) {  // 2, because "other" is always true...
          colors = [model.palette.colours.find(function(colour) {
            return colour.get('multiple') === true;
          })];
        }
        var point = new L.CircleMarker(latlng, {
          radius: self.currentRadius,
          fillColor: colors[0].hex(),
          color: '#333',
          weight: 1,
          opacity: 1,
          fillOpacity: 1
        });

        self.markerCluster.addLayer(point);

        return point;
      },
      onEachFeature: self._onEachFeature
    });
  },


  // Create pop-ups
  _onEachFeature: function(feature, layer) {
    var self = this;

    if (feature.properties) {
      //TODO: template:
      layer.bindPopup('Project #: '+ (feature.properties.activity ? feature.properties.activity[0] :'') +
        '<br />Site: ' + feature.properties.structureTitle );
    }

    layer.on('click', function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.projectId;
        self._hilightProject(projectId);
      }
    });

    layer.on('popupclose',function(evt) {
      var feature = evt.target.feature;
      if (feature) {
        var projectId = feature.properties.projectId;
        self._dehilightProject(projectId);
      }
    });
  },

  _hilightProject: function(projectId) {
    this.featureGroup.eachLayer(function(layer) {
      var properties = layer.feature.properties;
      if (properties.projectId === projectId) {
        layer.setStyle({fillColor: '#008'});
      }
    });
  },

  _dehilightProject: function(projectId) {
    this.featureGroup.eachLayer(function(layer) {
      var properties = layer.feature.properties;
      if (properties.projectId === projectId) {
        layer.setStyle({fillColor: '#f70'});
      }
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
  }

  // fetch returns the deferred object of the raw (non-parsed) response.
  // _getProjectSites: function(filter) {
  //   return this.collection.fetch({
  //    data: JSON.stringify(filter),
  //    type: 'POST',
  //    headers: { //needed to add this to fix amp 415 unsuported media type err, but most API's don;t require this...
  //       'Accept': 'application/json',
  //       'Content-Type': 'application/json'
  //     }
  //   }).fail(function(jqXHR, textStatus, errorThrown){
  //     console.error('failed ', jqXHR, textStatus, errorThrown);
  //   });
  // },

  // _filtersUpdated: function() {
  //   // TODO: 1. get all the filters using an event or service
  //   //      fitlers-view.js can iterate over array of filters, and ask each one to return it's filter key and value....
  //   var filterObj = {};
  //   var self = this;


  //   // Get the values for the map. Sample URL:
  //   // /rest/gis/cluster?filter="{"FiltersParams":{"params":[{"filterName":"adminLevel","filterValue":["Region"]}]}}"
  //   // (don't forget to url-encode)
  //   return this._getProjectSites(filterObj).then(function(data) {

  //     if (data && data.type === 'FeatureCollection') {
  //       self.features = data.features;
  //       self.rawData = data;
  //       self._renderFeatures();
  //     } else {
  //       console.warn('Project Sites response empty or improper type.');
  //     }
  //   });
  // },

});
