var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var MapHeaderView = require('../views/map-header-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../views/legend-view');
var ADMLayerView = require('../views/adm-layer-view');
var Basemaps = require('../models/basemap-collection');
var Template = fs.readFileSync(__dirname + '/../templates/map-container-template.html', 'utf8');

var state = require('../../services/state');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  initialize: function() {
    this.mapEl = $('<div id="map-canvas">');
    this.map = L.map(this.mapEl[0]);

    state.register(this, 'map', {
      get: this._getMapView,
      set: this._setMapView,
      empty: { center: [47.02, 28.60], zoom: 8 }
    });

    this.basemaps = new Basemaps();  // pre-loaded with hard-coded basemaps
    this.admLayerView = new ADMLayerView({map: this.map});

    this.headerView = new MapHeaderView();
    this.legendView = new LegendView();
    this.basemapView = new BasemapGalleryView({
      map: this.map,
      collection: this.basemaps
    });
  },

  render: function () {
    var self = this;
    this.$el.html(this.template({map: this.mapEl}));
    this.$el.append(this.mapEl);
    this.map.invalidateSize();

    var headerContainer = this.$('#map-header > div');
    headerContainer.append(this.headerView.render().el);
    headerContainer.append(this.basemapView.render().el);

    this.$el.append(this.legendView.render().el);

    this.loadBoundaries();
    Backbone.on('MAP_LOAD_INDICATOR', self._loadIndicatorLayer, this);

    return this;
  },

  _getMapView: function() {
    var center = this.map.getCenter();
    return {
      center: [
        center.lat,
        center.lng
      ],
      zoom: this.map.getZoom()
    };
  },

  _setMapView: function(stateBlob) {
    this.map.setView(stateBlob.center, stateBlob.zoom);
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
