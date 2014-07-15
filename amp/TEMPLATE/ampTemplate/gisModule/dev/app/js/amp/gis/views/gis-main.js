var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var MapView = require('../../map/views/main-view');
var DataQualityView = require('../../dataquality/views/dataquality-view');
var SidebarView = require('../../sidebar/sidebar-view');

var AmpNavTemplate = fs.readFileSync(__dirname + '/../templates/amp-nav-template.html', 'utf8');
var ModuleTemplate = fs.readFileSync(__dirname + '/../templates/module-template.html', 'utf8');


module.exports = Backbone.View.extend({

  initialize: function() {
    this.mapView = new MapView();
    this.dataQualityView = new DataQualityView();
    this.sidebarView = new SidebarView();
  },

  // Render entire geocoding view.
  render: function () {
    this.$el.html(ModuleTemplate);

    this.mapView.setElement(this.$('#map-container')).render();
    this.dataQualityView.setElement(this.$('#quality-indicator')).render();
    this.sidebarView.setElement(this.$('#sidebar-tools')).render();

    // just for testing...
    this.renderStaticAmpTemplate();
  },

  // not a view, because it's static and just for testing.
  renderStaticAmpTemplate: function(){
    $('#amp-menu').html(AmpNavTemplate);
  }
});
