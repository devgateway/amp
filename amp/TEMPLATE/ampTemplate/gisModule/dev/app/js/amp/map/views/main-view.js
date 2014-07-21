var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var MapHeaderView = require('../views/map-header-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../views/legend-view');

var ADMLayerView = require('../views/adm-layer-view');
var ProjectSitesLayerView = require('../views/project-sites-view');
var IndicatorLayerView = require('../views/indicator-layer-view');

var Basemaps = require('../collections/basemap-collection');
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
      empty: { center: [-3, 22], zoom: 6 }
    });

    this.basemaps = new Basemaps();  // pre-loaded with hard-coded basemaps

    // init layers
    this.projectSitesLayerView = new ProjectSitesLayerView({map: this.map});
    this.admLayerView = new ADMLayerView({map: this.map});
    this.indicatorLayerView = new IndicatorLayerView({map: this.map});

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

  // TODO: this is just a proof of concept
  loadBoundaries: function(){
    var self = this;

    // Styling tutorial: http://esri.github.io/esri-leaflet/examples/styling-feature-layer-polygons.html
    // Can do stuff with feature properties if we want. such as show ADM details on click.
    // TODO: Switch to AJAX style if same origin for boundary:  L.mapbox.featureLayer().loadURL('....');
    // DRC: http://gis.devgateway.org/arcgis/rest/services/wbi/Africa/MapServer/13'
    // Moldova: http://gis.devgateway.org/arcgis/rest/services/wbi/Europe_and_Central_Asia/MapServer/43

    // only do once
    if(!this.boundaryLayer){
      this.boundaryLayer = L.esri.featureLayer(
        'http://gis.devgateway.org/arcgis/rest/services/wbi/Africa/MapServer/13',
        {
          simplifyFactor: 0.9,
          style:  {color: 'blue', fillColor:'none', weight: 1}
        }).addTo(self.map);
    }
  },



});
