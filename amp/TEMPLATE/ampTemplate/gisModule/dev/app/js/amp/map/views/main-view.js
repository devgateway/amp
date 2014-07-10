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
        this.map = L.map('map-canvas').setView([11, 30], 4);
        this.admLayerView = new ADMLayerView({map: this.map});
    }

    // Render map header
    var headerView = new MapHeaderView();
    headerContainer.append(headerView.render().el);

    // Render BasemapGallery
    var basemapView = new BasemapGalleryView({map: this.map});
    headerContainer.append(basemapView.el);
    basemapView.render();  // the Basemap widget is special, it needs an
                           // on-page DOM node to work.

    // Render Legend
    var legendView = new LegendView();
    this.$el.append(legendView.render().el);

    this.loadBoundaries();

    return this;
  },

  // TODO: I'm  not sure of best place to put this. but just want to proof of concept for loading country layers...
  loadBoundaries: function(){

    // Styling tutorial: http://esri.github.io/esri-leaflet/examples/styling-feature-layer-polygons.html
    // Can do stuff with feature properties if we want.
    L.esri.featureLayer('http://gis.devgateway.org/arcgis/rest/services/wbi/Africa/MapServer/13',{
        simplifyFactor: 0.9,
        style:  {color: 'blue', weight: 2}
    }).addTo(this.map);

  }
});
