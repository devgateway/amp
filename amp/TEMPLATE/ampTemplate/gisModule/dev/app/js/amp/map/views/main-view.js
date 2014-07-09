var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var MapHeaderView = require('../views/map-header-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../views/legend-view');
var admLayerView = require('../views/adm-layer-view');
var Template = fs.readFileSync(__dirname + '/../templates/map-container-template.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  render: function () {

    this.$el.html(this.template({}));

    var headerContainer = this.$('#map-header > div');

    // Render the map
    // ESRI Leaflet Map
    var map = L.map('map-canvas').setView([-4, 24], 7);
    L.esri.basemapLayer("Gray").addTo(map);

    // var map = L.map('map-canvas').setView([-4, 24], 6);
    // L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png').addTo(map);




    // Render map header
    var headerView = new MapHeaderView();
    headerContainer.append(headerView.render().el);

    // Render BasemapGallery
    var basemapView = new BasemapGalleryView();
    headerContainer.append(basemapView.el);
    basemapView.render();  // the Basemap widget is special, it needs an
                           // on-page DOM node to work.

    // Render Legend
    var legendView = new LegendView();
    this.$el.append(legendView.render().el);

    return this;
  }
});
