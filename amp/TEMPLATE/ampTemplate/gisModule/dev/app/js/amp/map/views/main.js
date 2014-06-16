define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/map/templates/map-container.html",
    "esri/map",
  ],
  function (_, Backbone, Template, Map) {
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
          mapCanvas.height($(window).height() - mapCanvas.position().top-2);

          self.map = new Map('map-canvas', {
            center: [-56.049, 38.485],
            zoom: 3,
            basemap: "streets"

          });
        });
      }
    });

    return MapView;
  }
);
