var fs = require('fs');

var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');

var MapHeaderView = require('../views/map-header-view');
var BasemapGalleryView = require('../views/basemap-gallery-view');
var LegendView = require('../views/legend-view');

var Template = fs.readFileSync(__dirname + '/../templates/map-container-template.html', 'utf8');


module.exports = Backbone.View.extend({
  apiURL: 'js/mock-api/cluster.json', //'http://localhost:8080/rest/gis/cluster',

  template: _.template(Template),

  initialize: function(options) {

    // instead, maybe we can grab a reference to the model or collection,
    // backing the filter, and subscribe to changes on it?
    Backbone.on('FILTERS_UPDATED', this._filtersUpdated, this);

    // TODO: just for testing for now, so I force a trigger.
    Backbone.trigger('FILTERS_UPDATED');

  },

  render: function() {
    return this;
  },

  _filtersUpdated: function() {
    // TODO: Should only run if this layer is active.. check self.graphicLayer.active

    // TODO: 1. get all the filters using an event or service
    //      fitlers-view.js can iterate over array of filters, and ask each one to return it's filter key and value....
    var filterObj = {};
    var self = this;

    // Get the values for the map. Sample URL:
    // /rest/gis/cluster?filter="{"FiltersParams":{"params":[{"filterName":"adminLevel","filterValue":["Region"]}]}}"
    // (don't forget to url-encode)
    this._getCluster().then(function(data) {
      if(data && data.type === 'FeatureCollection') {
        self.features = data.features;
        self._renderFeatures();
      } else{
        console.warn('Cluster response empty.');
      }
    });
  },

  _renderFeatures: function() {
    var self = this;

    // TODO create an extent that has all of the points...
    // self.map.setExtent();
  },

  // Can do some post-processing here if we want...
  _getCluster: function(filter){
    // TODO: may need to encode filter....
    return $.ajax({
        type: 'GET',
        url: this.apiURL, // or mock api json
        data: filter
      });
  }
});
