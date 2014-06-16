define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/map/templates/map-container.html",
    "esri/map",
    'js/amp/map/views/mapHeaderInfo.js',
    'js/amp/map/views/basemapGallery.js'
  ],
  function (_, Backbone, Template, Map, MapHeaderView, BasemapGalleryView) {
    'use strict';

    var MapView = Backbone.View.extend({

      template: _.template(Template),

      initialize: function () {

      },

      render: function () {
        var self = this;

        this.$el.html(this.template({}));

        // Render ESRI map
        require(["esri/map"], function(Map) {
          var mapCanvas = self.$el.find('#map-canvas');
          mapCanvas.height(self.$el.height());  // TODO: adjust on window resize?
          self.map = new Map('map-canvas', {
            center: [-56.049, 38.485],
            zoom: 3,
            basemap: "streets"
          });
        });

        // Render map header
        var headerView = new MapHeaderView();
        var headerHTML = headerView.render();
        this.$el.find('.map-header').append(headerHTML);

        // Render BasemapGallery
        var basemapView = new BasemapGalleryView({map: self.map});
        var basemapHTML = basemapView.render();
        this.$el.find('#basemap-gallery').append(basemapHTML);

      }
    });

    return MapView;
  }
);
