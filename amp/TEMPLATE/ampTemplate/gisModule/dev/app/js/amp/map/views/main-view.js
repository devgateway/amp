var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var MapHeaderView = require('../views/map-header-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../views/legend-view');
var ADMLayerView = require('../views/adm-layer-view');
var Basemaps = require('../models/basemap-collection');
var Template = fs.readFileSync(__dirname + '/../templates/map-container-template.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  initialize: function() {
    this.basemaps = new Basemaps();

  },

  render: function () {
    var self = this;
    this.$el.html(this.template({}));

    var headerContainer = this.$('#map-header > div');

    // Render  map (but don't re-render if one exists)
    if(!this.map){
      this.map = L.map('map-canvas').setView([47.02, 28.60], 8);
      this.admLayerView = new ADMLayerView({map: this.map});
    }

    // Render map header
    var headerView = new MapHeaderView();
    headerContainer.append(headerView.render().el);

    // Render BasemapGallery
    var basemapView = new BasemapGalleryView({
      map: this.map,
      collection: this.basemaps
    });
    headerContainer.append(basemapView.render().el);

    // Render Legend
    var legendView = new LegendView();
    this.$el.append(legendView.render().el);

    this.loadBoundaries();
    Backbone.on('MAP_LOAD_INDICATOR', self._loadIndicatorLayer, this);

    return this;
  },

  // TODO: I'm  not sure of best place to put this. but just want to proof of concept
  loadBoundaries: function(){
    var self = this;

    // Styling tutorial: http://esri.github.io/esri-leaflet/examples/styling-feature-layer-polygons.html
    // Can do stuff with feature properties if we want. such as show ADM details on click.
    // TODO: Switch to AJAX style if same origin for boundary:  L.mapbox.featureLayer().loadURL('....');
    // DRC: http://gis.devgateway.org/arcgis/rest/services/wbi/Africa/MapServer/13'
    this.boundaryLayer = L.esri.featureLayer('http://gis.devgateway.org/arcgis/rest/services/wbi/Europe_and_Central_Asia/MapServer/43',{
        simplifyFactor: 0.9,
        style:  {color: 'blue', fillColor:'none', weight: 1}
      }).addTo(self.map);

  },

  // TODO: moveindicator layer should be own view under map.
  _loadIndicatorLayer: function (indicator) {
    var self = this;

    // remove current layer if its there:
    if(this.indicatorLayer){
      this.map.removeLayer(this.indicatorLayer);
    }

    // http://gis.devgw.org/arcgis/rest/services/Moldova
    if(indicator){
      var property = indicator.get('property');
      var maxValue = 0;

      this.indicatorLayer = L.esri.featureLayer(indicator.get('featurePath'),{
        simplifyFactor: 0.9,
        style:  function (feature) {
            maxValue = Math.max(maxValue, feature.properties[property]);
            return {color: 'blue',  weight: 1, opacity:0.4 };
          },
      }).addTo(this.map);


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
      var rgb = 'rgb(' + Math.round(Math.min(255,layer.feature.properties[property]*255/maxValue)) + ' ,' +
                          Math.round(Math.min(255,(layer.feature.properties[property]*255/maxValue))) + ',' +
                          Math.round(Math.min(255,(layer.feature.properties[property]*255/maxValue)+60)) + ')';
      self.indicatorLayer.setFeatureStyle(id, {color: rgb, fillColor:rgb, weight: 1, fillOpacity:0.5 });
    });
  }

});
