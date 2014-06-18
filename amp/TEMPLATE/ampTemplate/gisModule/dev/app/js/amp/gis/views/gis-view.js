define(
  [
    'underscore',
    'backbone',
    'js/amp/map/views/main-view.js',
    'js/amp/dataquality/views/dataquality-view.js',
    'js/amp/sidebar/sidebar-view.js'

  ],
  function (_, Backbone, MapView, DataQualityView, SidebarView) {
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

        var sidebarView = new SidebarView({el: '#sidebar-tools'});
        sidebarView.render();

      }
    });

    return GISView;
  }
);
