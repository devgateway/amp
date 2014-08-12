var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
require('../../../../../node_modules/leaflet.markercluster/dist/leaflet.markercluster.js');
var ProjectSitesCollection = require('../collections/project-sites-collection');

var ProjectListTemplate =  fs.readFileSync(__dirname + '/../templates/project-list-template.html', 'utf8');


module.exports = Backbone.View.extend({
  // These will eventually move to a config.
  // They control when to resize points based on zoom
  ZOOM_BREAKPOINT: 7,
  SMALL_ICON_RADIUS: 2,
  BIG_ICON_RADIUS: 7,
  currentRadius: 2,
  markerCluster: null,

  popup:null,
  projectListTemplate: _.template(ProjectListTemplate),

  initialize: function(extraProperties) {
    _.extend(this, extraProperties);  // extraProperties={map: ...}
    var self = this;
    L.Icon.Default.imagePath = '/img/map-icons';

    this.featureGroup = null;
    this.collection = new ProjectSitesCollection();

    //TODO: checkout prune cluster, supposedly way faster...
    // may also be worth doing manually since we don't want updates on zoom
    // TODO: make sizing dynamic based on highest cluster... and put into own function...
    this.markerCluster = new L.markerClusterGroup({
      maxClusterRadius: 1,
      iconCreateFunction: function (cluster) {
        var markers = cluster.getAllChildMarkers();
        var size = markers.length;
        // logarithmic in base 10:
        //var size =  Math.log(markers.length) / Math.LN10;
        size = 5 + size;

        // zoomed out. so no numbers.
        if(self.currentRadius === self.BIG_ICON_RADIUS){
          size+=5;
          return L.divIcon({ 
            html: markers.length, 
            className: 'marker-cluster', 
            iconSize: L.point(size, size) });

        } else{      
          var mark = new L.divIcon({ 
            html: '', 
            className: 'marker-cluster', 
            iconSize: L.point(size, size) });
         // mark.bindPopup('<div>' + markers.length + '</div>');
          return mark;
        }
      },
      zoomToBoundsOnClick:false,
      showCoverageOnHover:false,
      spiderfyOnMaxZoom:false,
      removeOutsideVisibleBounds:true
    });

    this.markerCluster.on('clusterclick', function (a) {
      //TODO: seems silly to bind on every click...
      a.layer.bindPopup(self.projectListTemplate({projects: a.layer.getAllChildMarkers()}));
      a.layer.openPopup(self.map);
    });

    this._filtersUpdated(); // explicitly call once at begining...maybe not needed later.

    // instead, maybe we can grab a reference to the model or collection,
    // backing the filter, and subscribe to changes on it?
    Backbone.on('FILTERS_UPDATED', this._filtersUpdated, this);
    Backbone.on('MAP_LOAD_PROJECT_LAYER', this._loadProjectLayer, this);

    _.bindAll(this, '_onEachFeature');
  },

  render: function() {
    return this;
  },


  _loadProjectLayer: function(type){
    if(type === 'locations'){
      this._addToMap();
      this.map.on('zoomend', this._updateZoom, this);
    } else{
      this._removeFromMap();
      this.map.off('zoomend',this._updateZoom);
    }
  },

  _filtersUpdated: function() {
    // TODO: 1. get all the filters using an event or service
    //      fitlers-view.js can iterate over array of filters, and ask each one to return it's filter key and value....
    var filterObj = {};
    var self = this;


    // Get the values for the map. Sample URL:
    // /rest/gis/cluster?filter="{"FiltersParams":{"params":[{"filterName":"adminLevel","filterValue":["Region"]}]}}"
    // (don't forget to url-encode)
    this._startLoadingIcon();
    return this._getProjectSites(filterObj).then(function(data) {
      self._stopLoadingIcon();

      if(data && data.type === 'FeatureCollection') {
        self.features = data.features;
        self.rawData = data;
        self._renderFeatures();
      } else{
        console.warn('Project Sites response empty or improper type.');
      }
    });
  },

  _renderFeatures: function() {
    var self = this;

    self.markerCluster.clearLayers();

    // add new featureGroup
    //console.log(JSON.stringify(self.rawData));
    self.featureGroup = L.geoJson(self.rawData , {
      pointToLayer: function (feature, latlng) {
        var point = new L.CircleMarker(latlng, {
            radius: self.currentRadius,
            fillColor: '#f70',
            color: '#000',
            weight: 1,
            opacity: 1,
            fillOpacity: 1,
          });

        self.markerCluster.addLayer(point);

        return point;
      },
      onEachFeature: self._onEachFeature
    });
  },

  _addToMap: function(){
    if(this.markerCluster && !this.map.hasLayer(this.markerCluster)){
      this.map.addLayer(this.markerCluster);
    }
  },

  _removeFromMap: function(){
    if(this.markerCluster && this.map.hasLayer(this.markerCluster)){
      this.map.removeLayer(this.markerCluster);
    }
  },


  // Create pop-ups
  _onEachFeature: function(feature, layer) {
    var self = this;

    if (feature.properties) {
      layer.bindPopup('Project #: '+ feature.properties.projectID  +'<br />Site: ' + feature.properties.title );
    }

    layer.on('click', function(evt){
      var feature = evt.target.feature;
      if(feature){
        var projectID = feature.properties.projectID;
        self._hilightProject(projectID);
      }
    });

    layer.on('popupclose',function(evt){
      var feature = evt.target.feature;
      if(feature){
        var projectID = feature.properties.projectID;
        self._dehilightProject(projectID);
      }
    });
  },

  _hilightProject: function(projectID){
    this.featureGroup.eachLayer(function(layer){
      var properties = layer.feature.properties;
      if(properties.projectID === projectID){
        layer.setStyle({fillColor: '#008'});
      }
    });
  },

  _dehilightProject: function(projectID){
    this.featureGroup.eachLayer(function(layer){
      var properties = layer.feature.properties;
      if(properties.projectID === projectID){
        layer.setStyle({fillColor: '#f70'});
      }
    });
  },

  // fetch returns the deferred object of the raw (non-parsed) response.
  _getProjectSites: function(filter){
    return this.collection.fetch({data: filter});
  },

  // circles  shrink if we're zoomed out, get big if zoomed in
  _updateZoom: function(){
    var self = this;

    if(this.featureGroup){
      var zoom = this.map.getZoom();
      // make small points
      if(zoom < this.ZOOM_BREAKPOINT && self.currentRadius !== self.SMALL_ICON_RADIUS){
        self.currentRadius = self.SMALL_ICON_RADIUS;
        this.featureGroup.eachLayer(function(layer){
          layer.setRadius(self.currentRadius);
        });
      } else if(zoom >= this.ZOOM_BREAKPOINT && self.currentRadius !== self.BIG_ICON_RADIUS){
        self.currentRadius = self.BIG_ICON_RADIUS;
        this.featureGroup.eachLayer(function(layer){
          layer.setRadius(self.currentRadius);
        });
      }
    }

  },

  // TODO: improve, so not global jQuery selectors..
  // move to be inside sidebar view, and use app.data.model.
  _startLoadingIcon: function(){
    $('#point-selector .loading-icon').show();
  },
  _stopLoadingIcon: function(){
    $('#point-selector .loading-icon').hide();
  },



});
