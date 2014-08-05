var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
require('../../../../../node_modules/leaflet.markercluster/dist/leaflet.markercluster.js');
var ProjectSitesCollection = require('../collections/project-sites-collection');


module.exports = Backbone.View.extend({
  // These will eventually move to a config. 
  // They control when to resize points based on zoom
  ZOOM_BREAKPOINT: 7,
  SMALL_ICON_RADIUS: 2,
  BIG_ICON_RADIUS: 7,

  initialize: function(extraProperties) {
    _.extend(this, extraProperties);  // extraProperties={map: ...}

    L.Icon.Default.imagePath = '/img/map-icons';

    this.featureGroup = null;
    this.collection = new ProjectSitesCollection();

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
      this._filtersUpdated();
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
    this._getProjectSites(filterObj).then(function(data) {
      self._stopLoadingIcon();
      if(data && data.type === 'FeatureCollection') {
        self.features = data.features;
        self._renderFeatures();
      } else{
        console.warn('Project Sites response empty.');
      }
    });
  },

  // TODO: improve, so not global jQuery selectors..
  // move to be inside sidebar view, and use app.data.model.
  _startLoadingIcon: function(){
    $('#point-selector .loading-icon').show();
  },
  _stopLoadingIcon: function(){
    $('#point-selector .loading-icon').hide();
  },


  _renderFeatures: function() {
    var self = this;

    // remove current featureGroup
    this._removeFromMap();

//     // "exports": "L",
    var markers = new L.markerClusterGroup({
      maxClusterRadius: 0.1,       
      iconCreateFunction: function (cluster) {
        var markers = cluster.getAllChildMarkers();
        return L.divIcon({ html: markers.length, className: 'marker-cluster', iconSize: L.point(16, 16) });
      },
      zoomToBoundsOnClick:false, 
      showCoverageOnHover:false, 
      spiderfyOnMaxZoom:false, 
      removeOutsideVisibleBounds:true
    });

    // add new featureGroup
    self.featureGroup = L.geoJson(self.features, {
      pointToLayer: function (feature, latlng) {
        var point = new L.CircleMarker(latlng, {
            radius: self.SMALL_ICON_RADIUS,
            fillColor: '#f70',
            color: '#000',
            weight: 1,
            opacity: 1,
            fillOpacity: 1,
          });

        //var point2 = new L.Marker(latlng);
        markers.addLayer(point);

        return point;
      },
      onEachFeature: self._onEachFeature
    });//.addTo(self.map);


    self.map.addLayer(markers);
    // set map bounds
    //this.map.fitBounds(self.featureGroup.getBounds());

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

  _removeFromMap: function(){
    if(this.featureGroup){
      this.map.removeLayer(this.featureGroup);
    }
  },

  // Owen asked for the circles to shrink if we're zoomed out and there are lots of points..
  // To hacky to do cleanly for now...
  _updateZoom: function(){
    var self = this;
    console.log('_updateZoom');    
    
    if(this.featureGroup){
      var zoom = this.map.getZoom();
      // make small points
      if(zoom < this.ZOOM_BREAKPOINT){
        this.featureGroup.eachLayer(function(layer){
            layer.setRadius(self.SMALL_ICON_RADIUS);
          });
      } else {
        this.featureGroup.eachLayer(function(layer){
            layer.setRadius(self.BIG_ICON_RADIUS);
          });
      }
    }

  },


});
