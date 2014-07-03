define(
  [
    'underscore',
    'backbone',
    'text!amp/map/templates/map-container-template.html',

    'esri/map',
    'esri/SpatialReference',

    'amp/map/views/map-header-view',
    'amp/map/views/basemap-gallery-view',
    'amp/map/views/legend-view',
    'amp/map/views/adm-layer-view'
  ],
  function (_, Backbone, Template,
            Map, SpatialReference,
            MapHeaderView, BasemapGalleryView, LegendView, ADMLayerView) {
    'use strict';

    var MapView = Backbone.View.extend({

      template: _.template(Template),

      render: function () {

        this.$el.html(this.template({}));

        // Create ESRI map (but don't re-render if one exists)
        if(!this.map){
          console.log(new SpatialReference({wkid:32662}));
          this.map = new Map('map-canvas', {
            center: [-4, 24],
            zoom: 6,
            basemap: 'streets',
            autoResize: true,
            spatialReference: new SpatialReference({wkid:4326})
          });
          this.admLayerView = new ADMLayerView({map: this.map});
        }
        var headerContainer = this.$('#map-header > div');

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

        return this;
      }
    });

    return MapView;
  }
);
