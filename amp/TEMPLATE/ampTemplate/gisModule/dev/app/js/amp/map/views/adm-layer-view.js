var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var ADMTemplate = fs.readFileSync(__dirname + '/../templates/map-adm-template.html', 'utf8');


module.exports = Backbone.View.extend({
  apiURL: 'http://localhost:8080/rest/gis/cluster', //'mock-api/cluster.json', //

  admTemplate: _.template(ADMTemplate),

  initialize: function(extraProperties) {
    _.extend(this, extraProperties);

    // instead, maybe we can grab a reference to the model or collection,
    // backing the filter, and subscribe to changes on it?
    Backbone.on('FILTERS_UPDATED', this._filtersUpdated, this);
    Backbone.on('MAP_LOAD_POINT_LAYER', this._loadProjectLayer, this);

  },

  render: function() {
    return this;
  },

  _loadProjectLayer: function(type){
    if(type === 'aggregated'){
      this._filtersUpdated();
    } else{
      this._removeFromMap();
    }
  },

  _filtersUpdated: function() {
    // TODO: Should only run if this layer is active.. check self.graphicLayer.active

    // TODO: 1. get all the filters using an event or service
    //      fitlers-view.js can iterate over array of filters, and ask each one to return it's filter key and value....
    var filterObj = {};
    var self = this;

    // Get the values for the map. Sample URL:
    // /rest/gis/cluster?filter="{"FiltersParams":{"params":[{"filterName":"adminLevel","filterValue":["Region"]}]}}"
    // (don't forget to url-encode)
    this._getCluster().then(function(data) {
      if(data && data.type === 'FeatureCollection') {
        self.features = data.features;
        self._renderFeatures();
      } else{
        console.warn('Cluster response empty.');
      }
    });
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

  // Create pop-ups
  _onEachFeature: function(feature, layer) {
      if (feature.properties) {
        var activities = feature.properties.activityid;
        layer.bindPopup(feature.properties.admName + ' has ' + activities.length +' projects');
      }
  },

  // Can do some post-processing here if we want...
  _getCluster: function(filter){
    // TODO: may need to encode filter....
    return $.ajax({
        type: 'GET',
        url: this.apiURL,
        data: filter
      });
  },

  _removeFromMap: function(){
    if(this.featureGroup){
      this.map.removeLayer(this.featureGroup);
    } 
  },
});
