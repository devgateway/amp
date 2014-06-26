define(
  [
    'underscore',
    'backbone',
    'text!amp/map/templates/map-container-template.html',

    'esri/map',

    'amp/map/views/map-header-view',
    'amp/map/views/basemap-gallery-view',
    'amp/map/views/legend-view'
  ],
  function (_, Backbone, Template,
            Map,
            MapHeaderView, BasemapGalleryView, LegendView) {
    'use strict';

    var MapView = Backbone.View.extend({

      template: _.template(Template),

      render: function () {

        this.$el.html(this.template({}));

        // Render ESRI map (but don't re-render if one exists)
        this.map = this.map || new Map('map-canvas', {
          center: [34.175185, -13.256563],
          zoom: 6,
          basemap: 'streets',
          autoResize: true
        });

        var headerContainer = this.$('#map-header > div');

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

    return MapView;
  }
);
