var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');
var Template = fs.readFileSync(__dirname + '/../templates/basemap-gallery-template.html', 'utf8');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  tagName: 'ul',
  id: 'basemap-gallery',
  className: 'nav navbar-nav navbar-right dropdown',

  initialize: function(options) {
    _.extend(this, options); // stores a reference to the map, and basemapLayer
    this.basemapLayer = null;
    this.layerLabels = null;

    // Setup default basemap
    this.defaultBasemap = 'Gray';       //TODO: will get from config service/state
    this.defaultBasemapLabels = false;  //TODO: will get from config service/state    
    this._setBasemap(this.defaultBasemap,  this.defaultBasemapLabels);

    _.bindAll(this, 'clickBasemap');
  },

  render: function() {
    var self = this;

    this.$el.html(this.template());

    // TODO: if possible do it in re-usable modular way, as a mini lib that can be used as a standalone plugin...
    this.$('.esriBasemapGalleryNode').click(self.clickBasemap);

    return this;
  },

  clickBasemap: function (event) {
    var basemap = $(event.currentTarget).data('basemap');
    var basemapLabels = $(event.currentTarget).data('basemaplabels');
    this._setBasemap(basemap, basemapLabels);
  },

  _setBasemap: function (basemapString, useLabels){
    // Remove current basemap
    if (this.basemapLayer) {
      this.map.removeLayer(this.basemapLayer);
    }

    // set basemap
    if(basemapString){
      this.basemapLayer = L.esri.basemapLayer(basemapString);
      this.map.addLayer(this.basemapLayer);
    }

    // Remove current labels
    if (this.layerLabels) {
      this.map.removeLayer(this.layerLabels);
    }

    // Add labels if needed
    if (useLabels) {
      this.layerLabels = L.esri.basemapLayer(basemapString + 'Labels');
      this.map.addLayer(this.layerLabels);
    }
  }
});
