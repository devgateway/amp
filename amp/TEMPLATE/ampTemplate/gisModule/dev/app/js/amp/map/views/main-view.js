define(
  [
    'underscore',
    'backbone',
    'text!' + APP_ROOT + '/amp/map/templates/map-container-template.html',

    'esri/map',

    'js/amp/map/views/map-header-view.js',
    'js/amp/map/views/basemap-gallery-view.js',
    'js/amp/map/views/legend-view.js'
  ],
  function (_, Backbone, Template,
            Map,
            MapHeaderView, BasemapGalleryView, LegendView) {
    'use strict';

    var MapView = Backbone.View.extend({

      template: _.template(Template),

      initialize: function () {

      },

      render: function () {
        var self = this;

        this.$el.html(this.template({}));

        // Render ESRI map
        self.map = new Map('map-canvas', {
          center: [34.175185, -13.256563],
          zoom: 6,
          basemap: 'streets',
          autoResize: true
        });

        // Render map header
        var headerView = new MapHeaderView({el: '.map-header'});
        headerView.render();

        // Render BasemapGallery
        var basemapView = new BasemapGalleryView({el: '#basemap-gallery', map: self.map});
        basemapView.render();

        // Render Legend
        var legendView = new LegendView({el: '#legend'});
        legendView.render();

      }
    });

    return MapView;
  }
);
