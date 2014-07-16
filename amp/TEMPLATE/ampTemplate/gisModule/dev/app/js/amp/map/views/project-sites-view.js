var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
var ProjectSitesCollection = require('../collections/project-sites-collection');


module.exports = Backbone.View.extend({

  initialize: function(options) {
    this.map = options.map;
    this.featureGroup = null;
    this.collection = new ProjectSitesCollection();

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
    // TODO: Should only run if this layer is active.. check something like self.graphicLayer.active

    // TODO: 1. get all the filters using an event or service
    //      fitlers-view.js can iterate over array of filters, and ask each one to return it's filter key and value....
    var filterObj = {};
    var self = this;

    // Get the values for the map. Sample URL:
    // /rest/gis/cluster?filter="{"FiltersParams":{"params":[{"filterName":"adminLevel","filterValue":["Region"]}]}}"
    // (don't forget to url-encode)
    this._getProjectSites().then(function(data) {
      if(data && data.type === 'FeatureCollection') {
        self.features = data.features;
        self._renderFeatures();
      } else{
        console.warn('Project Sites response empty.');
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
        var myIcon = L.divIcon({
          className: 'map-project-site-icon',
          iconSize: [10, 10]});
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
        layer.bindPopup('Project: ' + feature.properties.title );
      }
  },

  // fetch returns the deferred object of the raw (non-parsed) response.
  _getProjectSites: function(filter){
    return this.collection.fetch({filter:filter});
  }

});
