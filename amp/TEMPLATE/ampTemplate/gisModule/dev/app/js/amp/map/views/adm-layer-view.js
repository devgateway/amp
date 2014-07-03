define(
  [
    'underscore',
    'backbone',
    'jquery',
    'text!amp/map/templates/map-container-template.html',
    'amp/services/json-convertor',

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
  function (_, Backbone, $, Template, JsonConverters,  
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
        this.symbol = new SimpleFillSymbol().setStyle(SimpleFillSymbol.STYLE_SOLID).setColor('blue').outline.setColor('blue');

        Backbone.on('FILTERS_UPDATED', this._filtersUpdated, this);

        // TODO: just for testing for now, so I force a trigger.
        Backbone.trigger('FILTERS_UPDATED');
     
      },

      _filtersUpdated: function(){
        // TODO: Should only run if this layer is active.. check self.graphicLayer.active

        // TODO: 1. get all the filters using an event or service
        //      fitlers-view.js can iterate over array of filters, and ask each one to return it's filter key and value....
        var filterObj = {};
        var self = this;
        
        // Get the values for the map
        // Ex: http://localhost:8080/rest/gis/cluster?filter=%7B%22FiltersParams%22:%7B%22params%22:%5B%7B%22filterName%22:%22adminLevel%22,%22filterValue%22:%5B%22Region%22%5D%7D%5D%7D%7D   
        this._getCluster().then(function(data){
          if(data && data.type == 'FeatureCollection'){
            self.features = data.features;
            self._renderFeatures();
          } else{
            console.warn('Cluster response empty.');
          }
        });                               
      },

      _renderFeatures: function(){
        var self = this;

        _.each(this.features, function(feature){
          console.log(feature);
          var esriGeometry = self.convertGeoJSONToESRI(feature);
          var pt = new Point(esriGeometry.geometry, new SpatialReference({wkid:4326}));
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
      },

      // Convert geo json to esri format.
      convertGeoJSONToESRI: function(geoJSON){
        return new JsonConverters().geoJsonConverter().toEsri(geoJSON);
      },

      render: function () {


        return this;
      }
    });

    return View;
  }
);
