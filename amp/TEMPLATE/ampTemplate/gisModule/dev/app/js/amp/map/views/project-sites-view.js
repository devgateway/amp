var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');
var ProjectSitesCollection = require('../collections/project-sites-collection');


module.exports = Backbone.View.extend({

  initialize: function(extraProperties) {
    _.extend(this, extraProperties);  // extraProperties={map: ...}
    this.featureGroup = null;
    this.collection = new ProjectSitesCollection();

    // instead, maybe we can grab a reference to the model or collection,
    // backing the filter, and subscribe to changes on it?
    Backbone.on('FILTERS_UPDATED', this._filtersUpdated, this);
    Backbone.on('MAP_LOAD_POINT_LAYER', this._loadProjectLayer, this);
    _.bindAll(this, '_onEachFeature');
  },

  render: function() {
    return this;
  },


  _loadProjectLayer: function(type){
    if(type === 'locations'){
      this._filtersUpdated();
    } else{
      this._removeFromMap();
    }
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


    this._startLoadingIcon();
    this._getProjectSites().then(function(data) {
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
  // TODO: figure out why gif animation doesn't play while fetching..
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

    // add new featureGroup
    self.featureGroup = L.geoJson(self.features, {
      pointToLayer: function (feature, latlng) {
        return new L.CircleMarker(latlng, {
            radius: 4,
            fillColor: '#f70',
            color: '#000',
            weight: 1,
            opacity: 1,
            fillOpacity: 1,
          });
      },
      onEachFeature: self._onEachFeature
    }).addTo(self.map);

    // set map bounds
    this.map.fitBounds(self.featureGroup.getBounds());

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

  // Owen asked for the circles to shrink if we're zoomed out there are lots of points..
  // To hacky to do cleanly for now...
  // _updateZoom: function(){
  //   if(this.featureGroup){
  //     var zoom = this.map.getZoom();
  //     // make small points
  //     if(zoom < 9){
  //       this.featureGroup.eachLayer(function(layer){
  //           layer.setRadius(2);
  //       });
  //     } else {
  //       this.featureGroup.eachLayer(function(layer){
  //           layer.setRadius(5);
  //       });
  //     }
  //   }
  // },


});
