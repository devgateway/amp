var _ = require('underscore');
var Backbone = require('backbone');
var MapView = require('../../map/views/main-view');
var DataQualityView = require('../../dataquality/views/dataquality-view');
var SidebarView = require('../../sidebar/sidebar-view');

module.exports = Backbone.View.extend({

  initialize: function() {
    this.mapView = new MapView({el:'#map-container'});
    this.dataQualityView = new DataQualityView({el: '#quality-indicator'});
    this.sidebarView = new SidebarView({el: '#sidebar-tools'});
  },

  // Render entire geocoding view.
  render: function () {
    this.mapView.render();
    this.dataQualityView.render();
    this.sidebarView.render();
  }
});
