var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');


module.exports = Backbone.View.extend({

  initialize: function(extraProperties) {
    _.extend(this, extraProperties);
    this.visible = false;

    Backbone.on('MAP_LOAD_INDICATOR', this._loadIndicatorLayer, this);

  },

  render: function() {
    return this;
  },


  _loadIndicatorLayer: function (indicator) {
    var self = this;

    // remove current layer if its there:
    if(this.indicatorLayer){
      this.visible = false;
      this.map.removeLayer(this.indicatorLayer);
    }

    if(indicator){
      this.visible = true;

      if (indicator.get('type') === 'arcgis'){
        this._loadArcGISLayer(indicator);
      } else if(indicator.get('type') === 'geoJSON'){
        this._loadGeoJSONLayer(indicator);        
      } else{
        console.warn('No type matches this indicator', indicator);
      }
    }
  },

  _loadArcGISLayer: function(indicator){
    var self = this;
    var property = indicator.get('property');
    var maxValue = 0;

    this.indicatorLayer = L.esri.Layers.featureLayer(indicator.get('featurePath'),{
      simplifyFactor: 0.9,
      style:  function (feature) {
          maxValue = Math.max(maxValue, feature.properties[property]);
          return {color: 'blue',  weight: 1, opacity:0.4 };
        },
    }).addTo(self.map);

    // load is triggered after every pan, because on pan it asks for more feature info from server.
    // set all styles after they are on page and we know the max
    this.indicatorLayer.on('load', function(){
      self._updateESRIIndicatorStyles(maxValue, property);
    }); 
  },

  _loadGeoJSONLayer: function(indicator) {
    var self = this;
    var property = indicator.get('property');
    var maxValue = 0;

    //TODO: may need to apply API base url to begining of path
    $.get(indicator.get('featurePath')).then(function(geoJsonData){

      // create layer
      self.indicatorLayer = L.geoJson(
        geoJsonData, {
        style:  function (feature) {
            maxValue = Math.max(maxValue, feature.properties[property]);         
            return {color: 'blue',  weight: 1,  opacity:0.4, fillOpacity:0.5 };
          }
      }).addTo(self.map);
      self._updateIndicatorStyles(maxValue, property);
    });

  },

  // for esri indicator groups..
  _updateESRIIndicatorStyles: function(maxValue, property){
    var self = this;
    this.indicatorLayer.eachFeature(function(layer){
      var id = layer.feature.id;
      var colourInt = Math.round(Math.min(255,layer.feature.properties[property]*255/maxValue));
      var rgb = 'rgb(' + (colourInt + 60) + ' ,' +
                colourInt + ',' +
                colourInt + ')';
      self.indicatorLayer.setFeatureStyle(id, {color: rgb, fillColor:rgb, weight: 1, fillOpacity:0.5 });
    });
  },

  // For normal Leaflet feature group
  _updateIndicatorStyles: function(maxValue, property){
    this.indicatorLayer.setStyle(function(feature){

      var colourInt = Math.round(Math.min(255, feature.properties[property]*255/maxValue));
      var rgb = 'rgb(' + (colourInt + 60) + ' ,' +
                Math.max(colourInt-60,0) + ',' +
                Math.max(colourInt-60,0) + ')'; 
      return {color: rgb, fillColor:rgb, weight: 1, fillOpacity:0.5 };
    });
  },  

});
