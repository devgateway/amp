define(
  [
    'underscore',
    'backbone',
    'text!' + APP_ROOT + '/amp/map/templates/map-container.html',
    'esri/map',
    'js/amp/map/views/mapHeaderInfo.js',
    'js/amp/map/views/basemapGallery.js',
    'js/amp/map/views/legend.js'
  ],
  function (_, Backbone, Template, Map, MapHeaderView, BasemapGalleryView, LegendView) {
    'use strict';

    var MapView = Backbone.View.extend({

      template: _.template(Template),

      initialize: function () {

      },

      render: function () {
        var self = this;

        this.$el.html(this.template({}));

        // Render ESRI map
        require(['esri/map'], function(Map) {
          var mapCanvas = self.$el.find('#map-canvas');
          mapCanvas.height(self.$el.height());  // TODO: adjust on window resize?
          self.map = new Map('map-canvas', {
            center: [-56.049, 38.485],
            zoom: 3,
            basemap: 'streets'
          });
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
