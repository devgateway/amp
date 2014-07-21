var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
var ADMClusterCollection = require('../collections/adm-cluster-collection');

var ADMTemplate = fs.readFileSync(__dirname + '/../templates/map-adm-template.html', 'utf8');


module.exports = Backbone.View.extend({
  features: null,
  featureGroup: null,
  boundaryLayer: null,

  admTemplate: _.template(ADMTemplate),

  initialize: function(extraProperties) {
    _.extend(this, extraProperties);
    this.collection = new ADMClusterCollection();


    // TODO: move listener to collection, and subscribe to it's changes.
    Backbone.on('FILTERS_UPDATED', this._filtersUpdated, this);
    Backbone.on('MAP_LOAD_POINT_LAYER', this._loadProjectLayer, this);

  },

  render: function() {
    return this;
  },

  _renderFeatures: function() {
    var self = this;

    // remove current featureGroup
    if(self.featureGroup){
      self.map.removeLayer(self.featureGroup);
    } 

    // add new featureGroup
    self.featureGroup = L.geoJson(self.features, {
      pointToLayer: function (feature, latlng) {
        var htmlString = self.admTemplate(feature);
        var myIcon = L.divIcon({
          className: 'map-adm-icon',  
          html: htmlString,
          iconSize: [60, 50]});
        return L.marker(latlng, {icon: myIcon});//L.circleMarker(latlng, geojsonMarkerOptions);
      },
      onEachFeature: self._onEachFeature
    }).addTo(self.map);

    // set map bounds
    self.map.fitBounds(self.featureGroup.getBounds());

  },

  // TODO: non hardcoded version.
  _loadProjectLayer: function(type){
    if(type === 'aggregate-adm1'){
      this._filtersUpdated({adminLevel: 1});
    } else if(type === 'aggregate-adm2'){
      this._filtersUpdated({adminLevel: 2});
    } else {
      this._removeFromMap();
    }
  },

  _filtersUpdated: function(filterObj) {
    // TODO: Should only run if this layer is active.. check for equiv of self.graphicLayer.active
    var self = this;

    this._loadBoundaries(filterObj);

    // Get the values for the map. Sample URL:
    // /rest/gis/cluster?filter="{"FiltersParams":{"params":[{"filterName":"adminLevel","filterValue":["Region"]}]}}"
    // (don't forget to url-encode)
    this._getCluster(filterObj).then(function(data) {
      if(data && data.type === 'FeatureCollection') {
        self.features = data.features;
        self._renderFeatures();
      } else{
        console.warn('Cluster response empty.');
      }
    });
  },

  // TODO: this is just a proof of concept
  _loadBoundaries: function(filterObj){
    var self = this;

    // TODO: Switch to AJAX style if same origin for boundary:  L.mapbox.featureLayer().loadURL('....');
    // DRC: http://gis.devgateway.org/arcgis/rest/services/wbi/Africa/MapServer/13'
    // Moldova: http://gis.devgateway.org/arcgis/rest/services/wbi/Europe_and_Central_Asia/MapServer/43

    // only do once
    this._removeBoundary();

    // TODO: use filterObj.adminLevel to choose right boundary.
    // create boundry
    this.boundaryLayer = L.esri.featureLayer(
      'http://gis.devgateway.org/arcgis/rest/services/wbi/Africa/MapServer/13',
      {
        simplifyFactor: 0.9,
        style:  {color: 'blue', fillColor:'none', weight: 1, dashArray: '3',}
      }).addTo(self.map);
    
  },

  _removeBoundary: function(){
    if(this.boundaryLayer){
      this.map.removeLayer(this.boundaryLayer);
    }
  },

  // Create pop-ups
  _onEachFeature: function(feature, layer) {
      if (feature.properties) {
        var activities = feature.properties.activityid;
        layer.bindPopup(feature.properties.admName + ' has ' + activities.length +' projects');
      }
  },

  // Can do some post-processing here if we want...
  _getCluster: function(filter){    
    return this.collection.fetch({data:filter});
  },

  _removeFromMap: function(){
    this._removeBoundary();

    if(this.featureGroup){
      this.map.removeLayer(this.featureGroup);
    } 
  },
});
