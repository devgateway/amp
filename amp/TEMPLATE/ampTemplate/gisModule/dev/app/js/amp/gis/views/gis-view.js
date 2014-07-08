var _ = require('underscore');
var Backbone = require('backbone');
var MapView = require('../../map/views/main-view');
var DataQualityView = require('../../dataquality/views/dataquality-view');
var SidebarView = require('../../sidebar/sidebar-view');

module.exports = Backbone.View.extend({
  // Render entire geocoding view.
  render: function () {

    var mapView = new MapView({el:'#map-container'});
    mapView.render();

    var dataQualityView = new DataQualityView({el: '#quality-indicator'});
    dataQualityView.render();

    var sidebarView = new SidebarView({el: '#sidebar-tools'});
    sidebarView.render();

  }
});
