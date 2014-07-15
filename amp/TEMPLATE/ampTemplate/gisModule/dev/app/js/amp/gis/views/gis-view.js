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
    this.$el.html(ModuleTemplate);
    
    this.mapView = new MapView({el:'#map-container'});
    this.dataQualityView = new DataQualityView({el: '#quality-indicator'});
    this.sidebarView = new SidebarView({el: '#sidebar-tools'});
  },

  // Render entire geocoding view.
  render: function () {
    this.mapView.render();
    this.dataQualityView.render();
    this.sidebarView.render();

    // just for testing...
    this.renderStaticAmpTemplate();
  },

  // not a view, because it's static and just for testing.
  renderStaticAmpTemplate: function(){
    $('#amp-menu').html(AmpNavTemplate);
  }
});
