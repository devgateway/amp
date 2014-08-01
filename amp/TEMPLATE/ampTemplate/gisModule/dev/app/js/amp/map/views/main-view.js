var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var APIHelper = require('../../../libs/local/api-helper');

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

  initialize: function(options) {
    this.app = options.app;
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

    this.headerView = new MapHeaderView({app: this.app});
    this.legendView = new LegendView({app: this.app});
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

    this._renderCountryBoundary();

    return this;
  },


  _renderCountryBoundary: function(){
    var self = this;
    // TODO: harcoded path is bad.
    $.get( APIHelper.getAPIBase() + '/rest/gis/boundaries/adm0').then(function(geoJSON){

      self.countryBoundary = L.geoJson(geoJSON,
        {
          simplifyFactor: 0.9,
          style:  {color: 'blue', fillColor:'none', weight: 1, dashArray: '3',}
        }).addTo(self.map);
    });
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

});
