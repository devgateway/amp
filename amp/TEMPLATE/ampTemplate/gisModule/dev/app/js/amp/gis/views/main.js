define(
  [
    'underscore',
    'backbone',
    'js/amp/map/views/main.js',
    'js/amp/dataquality/views/main.js',
    'js/amp/tools/view.js'

  ],
  function (_, Backbone, MapView, DataQualityView, ToolsView) {
    'use strict';

    var GISView = Backbone.View.extend({

      initialize: function () {

        console.log('init');

      },

      // Render entire geocoding view.
      render: function () {

        console.log('render');
        var mapView = new MapView({el:'#map-container'});
        mapView.render();

        var dataQualityView = new DataQualityView({el: '#quality-indicator'});
        dataQualityView.render();

        var toolsView = new ToolsView({el: '#sidebar-tools'});
        toolsView.render();

      }
    });

    return GISView;
  }
);
