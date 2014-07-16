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
      var property = indicator.get('property');
      var maxValue = 0;


      this.indicatorLayer = L.esri.featureLayer(indicator.get('featurePath'),{
        simplifyFactor: 0.9,
        style:  function (feature) {
            maxValue = Math.max(maxValue, feature.properties[property]);
            return {color: 'blue',  weight: 1, opacity:0.4 };
          },
      }).addTo(self.map);


      // set all styles after they are on page and we know the max
      this.indicatorLayer.on('load', function(){
        self._updateIndicatorStyles(maxValue, property);
      });
    }
  },



  // TODO: moveindicator layer should be own view under map.
  _updateIndicatorStyles: function(maxValue, property){
    var self = this;
    this.indicatorLayer.eachFeature(function(layer){
      var id = layer.feature.id;
      var rgb = 'rgb(' + Math.round(Math.min(255,layer.feature.properties[property]*255/maxValue)+60) + ' ,' +
                          Math.round(Math.min(255,(layer.feature.properties[property]*255/maxValue))) + ',' +
                          Math.round(Math.min(255,(layer.feature.properties[property]*255/maxValue))) + ')';
      self.indicatorLayer.setFeatureStyle(id, {color: rgb, fillColor:rgb, weight: 1, fillOpacity:0.5 });
    });
  },

});
