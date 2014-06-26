define(
  [
    'underscore',
    'backbone',
    'amp/map/views/main-view',
    'amp/dataquality/views/dataquality-view',
    'amp/sidebar/sidebar-view'
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
