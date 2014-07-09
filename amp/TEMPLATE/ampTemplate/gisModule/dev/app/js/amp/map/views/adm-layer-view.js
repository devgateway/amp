var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var MapHeaderView = require('../views/map-header-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../views/legend-view');

var ADMTemplate = fs.readFileSync(__dirname + '/../templates/map-adm-template.html', 'utf8');


module.exports = Backbone.View.extend({
  apiURL: 'js/mock-api/cluster.json', //'http://localhost:8080/rest/gis/cluster',

  admTemplate: _.template(ADMTemplate),

  initialize: function(options) {
    L.Icon.Default.imagePath = 'img/map-icons';
    this.map = options.map;

    // instead, maybe we can grab a reference to the model or collection,
    // backing the filter, and subscribe to changes on it?
    Backbone.on('FILTERS_UPDATED', this._filtersUpdated, this);

    // TODO: just for testing for now, so I force a trigger.
    Backbone.trigger('FILTERS_UPDATED');

  },

  render: function() {
    return this;
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

    L.geoJson(self.features, {
      pointToLayer: function (feature, latlng) {
        var htmlString = self.admTemplate(feature.geometry.properties);
        var myIcon = L.divIcon({
          className: 'map-adm-icon',  
          html: htmlString,
          iconSize: [50, 50]});
        return L.marker(latlng, {icon: myIcon});//L.circleMarker(latlng, geojsonMarkerOptions);
      },
      onEachFeature: self._onEachFeature
    }).addTo(self.map);

  },

  // Create pop-ups for given feature of a layer
  _onEachFeature: function(feature, layer) {
      if (feature.geometry.properties) {
        var activities = feature.geometry.properties.activityid.split(',');
        layer.bindPopup(feature.geometry.properties.adm + ' has ' + activities.length +' projects');
      }
  },

  // Can do some post-processing here if we want...
  _getCluster: function(filter){
    // TODO: may need to encode filter....
    return $.ajax({
        type: 'GET',
        url: this.apiURL, // or mock api json
        data: filter
      });
  }
});
