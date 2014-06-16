define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/map/templates/map-container.html",
    "esri/map",
    'js/amp/map/views/basemapGallery.js'
  ],
  function (_, Backbone, Template, Map, BaseMapView) {
    'use strict';

    var MapView = Backbone.View.extend({

      template: _.template(Template),

      initialize: function () {

      },

      render: function () {
        var self = this;

        this.$el.html(this.template({}));

        // Render ESRI map
        require(["esri/map", "dojo/domReady!"], function(Map) {
          var mapCanvas = self.$el.find('#map-canvas');
          mapCanvas.height(self.$el.height());  // TODO: adjust on window resize?

          self.map = new Map('map-canvas', {
            center: [-56.049, 38.485],
            zoom: 3,
            basemap: "streets"

          });
        });

        // Render BasemapGallery
        var basemapView = new BaseMapView({map: self.map});
        this.$el.find('#map-header').append(basemapView.render());

      }
    });

    return MapView;
  }
);
