var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var APIHelper = require('../../../libs/local/api-helper');

var ADMClusterCollection = require('../collections/adm-cluster-collection');
var ADMTemplate = fs.readFileSync(__dirname + '/../templates/map-adm-template.html', 'utf8');


module.exports = Backbone.View.extend({
  features: null,
  featureGroup: null,
  boundaryLayer: null,

  admTemplate: _.template(ADMTemplate),

  initialize: function(options) {
    this.app = options.app;
    this.map = options.map;
    this.collection = new ADMClusterCollection();


    // TODO: move listener to collection, and subscribe to it's changes.
    Backbone.on('FILTERS_UPDATED', this._filtersUpdated, this);
    Backbone.on('MAP_LOAD_PROJECT_LAYER', this._loadProjectLayer, this);

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
          iconSize: [60, 50]
        });
        return L.marker(latlng, {icon: myIcon});//L.circleMarker(latlng, geojsonMarkerOptions);
      },
      onEachFeature: self._onEachFeature
    }).addTo(self.map);

    // set map bounds
    self.map.fitBounds(self.featureGroup.getBounds());

  },

  // TODO: non hardcoded version.
  _loadProjectLayer: function(type){
    if(type === 'adm-1'){
      this._filtersUpdated({adminLevel: type});
    } else if(type === 'adm-2'){
      this._filtersUpdated({adminLevel: type});
    } else {
      this._removeFromMap();
    }
  },

  _filtersUpdated: function(filterObj) {
    // TODO: Should only run if this layer is active.. check for equiv of self.graphicLayer.active
    var self = this;

    this._loadBoundaries(filterObj);

    // Get the values for the map.
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
  // Phil: do you use boundaries for joining?
  // I'm considering making boundary-view on the map where it just listens to the app.data collection,
  // and draws the selected / active boundary....
  _loadBoundaries: function(filterObj){
    var self = this;

    this._removeBoundary();

    if(filterObj.adminLevel){
      // get current boundary.
      var boundaries = this.app.data.boundaries;
      var currentBoundary = boundaries.findWhere({id:filterObj.adminLevel});
      if(currentBoundary){
        var geoJSON = currentBoundary.toJSON();
      } else{
        console.warn('no boundary found');
      }

      // David -- I commented this out because it broke from my indicator layer changes.
      // I wasn't sure if this was likely to change or be fixed as-is.
      // self.boundaryLayer = L.geoJson(geoJSON,
      //   {
      //     simplifyFactor: 0.9,
      //     style:  {color: 'blue', fillColor:'none', weight: 1, dashArray: '3',}
      //   }).addTo(self.map);
    } else{
      console.warn('missing admin level in Filter');
    }
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

    filter.adminLevel = this._tempConvertToString(filter.adminLevel);

    return this.collection.fetch({
        data: JSON.stringify(filter),
        type: 'POST',    
        headers: { //needed to add this to fix amp 415 unsuported media type err, but most API's don;t require this...
            'Accept': 'application/json',
            'Content-Type': 'application/json' 
          }
        }).fail(function(jqXHR, textStatus, errorThrown){
          console.error('failed ', jqXHR, textStatus, errorThrown);
        });
  },

  // !temp convert adminLevel to AMP strings:
  _tempConvertToString: function(id){
    var str = '';
    if(id ==='adm-0'){
      str = 'Country';
    } else if(id ==='adm-1'){
      str = 'Region';
    } else if(id ==='adm-2'){
      str = 'Zone';
    } else if(id ==='adm-3'){
      str = 'District';
    }
    return str;
  },


  _removeFromMap: function(){
    this._removeBoundary();

    if(this.featureGroup){
      this.map.removeLayer(this.featureGroup);
    }
  },
});
