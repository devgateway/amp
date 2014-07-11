var _ = require('underscore');
var Backbone = require('backbone');
var Basemap = require('./basemap-model');

var app = require('../../main');


var basemaps = [
  {
    id: 'Streets',
    source: 'esri',
    esriId: 'Streets',
    thumb: '/img/basemaps/world_street_map.jpg'
  },
  {
    id: 'Empty Basemap',
    source: null,
    thumb: '/img/basemaps/empty.png'
  },
  {
    id: 'Imagery',
    source: 'esri',
    esriId: 'Imagery',
    thumb: '/img/basemaps/tempimagery.jpg'
  },
  {
    id: 'Labeled Imagery',
    source: 'esri',
    esriId: 'Imagery',
    label: true,
    thumb: '/img/basemaps/tempimagery_with_labels_ne_usa.png'
  },
  {
    id: 'Topographic',
    source: 'esri',
    esriId: 'Topographic',
    thumb: '/img/basemaps/topo_map_2.jpg'
  },
  {
    id: 'Labeled Terrain',
    source: 'esri',
    esriId: 'Terrain',
    label: true,
    thumb: 'img/basemaps/tempTerrain_with_labels_ne_usa.png',
  },
  {
    id: 'Gray',
    source: 'esri',
    esriId: 'Gray',
    thumb: '/img/basemaps/templight_gray_canvas_with_labels__ne_usa.png'
  },
  {
    id: 'National Geographic',
    source: 'esri',
    esriId: 'NationalGeographic',
    thumb: '/img/basemaps/natgeo.jpg'
  },
  {
    id: 'Oceans',
    source: 'esri',
    esriId: 'Oceans',
    thumb: '/img/basemaps/tempoceans.jpg'
  },
  {
    id: 'Open Street Map',
    source: 'tile',
    tileUrl: 'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
    thumb: '/img/basemaps/temposm.jpg'
  }
];


module.exports = Backbone.Collection.extend({

  model: Basemap,

  initialize: function() {
    this.add(basemaps);
    this.currentlySelected = null;
    app.state.register('basemap', {
      get: _.bind(function() { return this.currentlySelected.id; }, this),
      set: _.bind(this.selectBasemap, this)
    }, 'Gray');
  },

  selectBasemap: function(basemapId) {
    if (this.currentlySelected) {
      this.currentlySelected.set('selected', false);
    }
    var newSelection = this.find(function(bmap) { return bmap.id === basemapId; });
    newSelection.set('selected', true);
    this.currentlySelected = newSelection;
  },

});
