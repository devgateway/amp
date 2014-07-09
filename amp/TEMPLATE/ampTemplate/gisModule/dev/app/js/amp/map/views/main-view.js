var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var MapHeaderView = require('../views/map-header-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../views/legend-view');
var ADMLayerView = require('../views/adm-layer-view');
var Template = fs.readFileSync(__dirname + '/../templates/map-container-template.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  render: function () {

    this.$el.html(this.template({}));

    var headerContainer = this.$('#map-header > div');

    // Render  map (but don't re-render if one exists)
    if(!this.map){
        this.map = L.map('map-canvas').setView([24, -4], 5);
        L.esri.basemapLayer('Gray').addTo(this.map);
        this.admLayerView = new ADMLayerView({map: this.map});
    }


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
