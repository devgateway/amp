define(
  [
    'underscore',
    'backbone',
    'jquery',
    'text!amp/map/templates/map-container-template.html',

    'convert/geo',

    'esri/map',
    'esri/SpatialReference',
    'esri/geometry/jsonUtils',
    'esri/symbols/SimpleFillSymbol',
    'dojo/_base/Color',
    'esri/graphic',
    'esri/layers/GraphicsLayer',
    'esri/geometry/Point',
    'esri/geometry/Circle',

    'amp/map/views/map-header-view',
    'amp/map/views/basemap-gallery-view',
    'amp/map/views/legend-view'
  ],
  function (_, Backbone, $, Template,
            convertGeo,
            Map, SpatialReference, JsonUtils, SimpleFillSymbol, Color, Graphic, GraphicsLayer, Point, Circle,
            MapHeaderView, BasemapGalleryView, LegendView) {
    'use strict';

    var View = Backbone.View.extend({
      apiURL: 'js/mock-api/cluster.json', //'http://localhost:8080/rest/gis/cluster',

      template: _.template(Template),

      initialize: function (options) {
        // set map.
        this.map = options.map;
        this.graphicLayer = new GraphicsLayer({ id: 'adm' });
        this.map.addLayer(this.graphicLayer);
        this.symbol = new SimpleFillSymbol()
          .setStyle(SimpleFillSymbol.STYLE_SOLID)
          .setColor('blue')
          .outline
            .setColor('blue');

        // instead, maybe we can grab a reference to the model or collection,
        // backing the filter, and subscribe to changes on it?
        Backbone.on('FILTERS_UPDATED', this._filtersUpdated, this);

        // TODO: just for testing for now, so I force a trigger.
        Backbone.trigger('FILTERS_UPDATED');

      },

      render: function () {
        return this;
      },

      _filtersUpdated: function(){
        // TODO: Should only run if this layer is active.. check self.graphicLayer.active

        // TODO: 1. get all the filters using an event or service
        //      fitlers-view.js can iterate over array of filters, and ask each one to return it's filter key and value....
        var filterObj = {};
        var self = this;

        // Get the values for the map. Sample URL:
        // /rest/gis/cluster?filter="{"FiltersParams":{"params":[{"filterName":"adminLevel","filterValue":["Region"]}]}}"
        // (don't forget to url-encode)
        this._getCluster().then(function(data){
          if(data && data.type === 'FeatureCollection'){
            self.features = data.features;
            self._renderFeatures();
          } else{
            console.warn('Cluster response empty.');
          }
        });
      },

      _renderFeatures: function(){
        var self = this,
            spacialReference = new SpatialReference({wkid:4326});

        // TODO: this.features could be a backbone collection, so we could do
        // this.collection.each(function(feature) { ... })
        _.each(this.features, function(feature){
          console.log(feature);
          var esriGeometry = convertGeo.geoJSONToESRI(feature);
          var pt = new Point(esriGeometry.geometry, spacialReference);
          var radius = 30000;
          var circle = new Circle({
            center: pt,
            radius: radius
          });

          var graphic = new Graphic(circle, self.symbol);
          self.graphicLayer.add(graphic);
        });

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

    return View;
  }
);
