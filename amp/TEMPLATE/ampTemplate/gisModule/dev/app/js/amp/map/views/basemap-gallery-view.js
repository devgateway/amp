var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var L = require('../../../../../node_modules/esri-leaflet/dist/esri-leaflet.js');

var Template = fs.readFileSync(__dirname + '/../templates/basemap-gallery-template.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  tagName: 'ul',
  id: 'basemap-gallery',
  className: 'nav navbar-nav navbar-right dropdown',

  events: {
    'click .esriBasemapGalleryNode': 'clickBasemap',
  },

  initialize: function(extraProperties) {
    _.extend(this, extraProperties);  // extraProperties={map: ...}
    this.listenTo(this.collection, 'change:selected', this.updateSelected);
    this.setBasemap(this.collection.currentlySelected); // since we missed the event
  },

  render: function() {
    this.$el.html(this.template({basemaps: this.collection}));
    return this;
  },

  clickBasemap: function(e) {
    var newId = e.currentTarget.dataset.id;
    this.collection.selectBasemap(newId);
  },

  updateSelected: function(basemap, selectedNow) {
    this[selectedNow ? 'setBasemap' : 'removeBasemap'](basemap);
  },

  setBasemap: function(basemap) {
    if (this.mapLayer) {
      throw new Error('Tried to set a new basemap layer before clearing the old one');
    }
    var source = basemap.get('source');
    if (source === 'esri') {
      var basemapId = basemap.get('esriId');
      this.mapLayer = L.esri.basemapLayer(basemapId);
      this.map.addLayer(this.mapLayer);
      if (basemap.get('label')) {
        this.labelsLayer = L.esri.basemapLayer(basemapId + 'Labels');
        this.map.addLayer(this.labelsLayer);
      }
    } else if (source === 'tile') {
      this.mapLayer = L.tileLayer(basemap.get('tileUrl'));
      this.map.addLayer(this.mapLayer);
    } else if (source !== null) {
      console.warn('layers from source ', source, 'not implemented');
    }
    // source === null is just an empty basemap, nothing to do
  },

  removeBasemap: function(basemap) {
    if (this.mapLayer) {
      this.map.removeLayer(this.mapLayer);
      delete this.mapLayer;
    }
    
    if (this.labelsLayer) {
      this.map.removeLayer(this.labelsLayer);
      delete this.labelsLayer;
    }
  }
});
