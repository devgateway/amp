var Backbone = require('backbone');
var BasemapModel = require('../models/basemap-model');


var BASEMAPS = [
  {
    id: 'Streets',
    source: 'esri',
    esriId: 'Streets',
    thumb: 'img/basemaps/world_street_map.jpg'
  },
  {
    id: 'Empty Basemap',
    source: null,
    thumb: 'img/basemaps/empty.png'
  },
  {
    id: 'Imagery',
    source: 'esri',
    esriId: 'Imagery',
    thumb: 'img/basemaps/tempimagery.jpg'
  },
  {
    id: 'Labeled Imagery',
    source: 'esri',
    esriId: 'Imagery',
    label: true,
    thumb: 'img/basemaps/tempimagery_with_labels_ne_usa.png'
  },
  {
    id: 'Topographic',
    source: 'esri',
    esriId: 'Topographic',
    thumb: 'img/basemaps/topo_map_2.jpg'
  },
  {
    id: 'Labeled Terrain',
    source: 'esri',
    esriId: 'Terrain',
    label: true,
    thumb: 'img/basemaps/tempTerrain_with_labels_ne_usa.png'
  },
  {
    id: 'Gray',
    source: 'esri',
    esriId: 'Gray',
    thumb: 'img/basemaps/templight_gray_canvas_with_labels__ne_usa.png'
  },
  {
    id: 'DarkGrayLabels',
    source: 'esri',
    esriId: 'DarkGray',
    label: true,
    thumb: 'img/basemaps/darkgray.jpg'
  },
  {
    id: 'National Geographic',
    source: 'esri',
    esriId: 'NationalGeographic',
    thumb: 'img/basemaps/natgeo.jpg'
  },
  {
    id: 'Oceans',
    source: 'esri',
    esriId: 'Oceans',
    thumb: 'img/basemaps/tempoceans.jpg'
  },
  {
    id: 'Open Street Map',
    source: 'tile',
    tileUrl: 'http://{s}.tile.osm.org/{z}/{x}/{y}.png',
    thumb: 'img/basemaps/temposm.jpg'
  }
];


module.exports = Backbone.Collection.extend({

  model: BasemapModel,

  initialize: function(models, options) {
    this.add(BASEMAPS);
    options.app.state.register(this, 'basemap', {
      get: function() {
        return this.getBasemap().id;
      },
      set: this.selectBasemap,
      empty: 'Open Street Map'
    });
  },

  getBasemap: function(name) {
    return this.find(function(bmap) {
      if (name) {
        return bmap.id === name;  // find by name
      } else {
        return bmap.get('selected');  // (selected is bool) get currently selected
      }
    });
  },

  selectBasemap: function(basemapId) {

    var newSelection = this.getBasemap(basemapId);
    if (!newSelection) {
      throw new Error('No basemap: ' + basemapId);
    }

    var oldSelection = this.getBasemap();
    if (oldSelection) {  // first time around there will be nothing selected
      oldSelection.set('selected', false);  // deselect the old
    }

    newSelection.set('selected', true);  // select the new one
  }

});
