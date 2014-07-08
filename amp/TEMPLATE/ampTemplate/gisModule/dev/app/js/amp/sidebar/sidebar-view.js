var _ = require('underscore');
var Backbone = require('backbone');
var LayersView = require('./layers/views/layers-view');
var FiltersView = require('./filters/views/filters-view');
var SearchView = require('./search/views/search-view');
var ToolsView = require('./tools/views/tools-view');
var SettingsView = require('./settings/views/settings-view');


var controlViews = [
  LayersView,
  SearchView,
  FiltersView,
  ToolsView,
  SettingsView
];

module.exports = Backbone.View.extend({

  events: {
    'hide.bs.collapse .sidebar-tool': 'hidePopovers',
    'show.bs.popover .layer-info': 'blah'
  },

  hidePopovers: function() {
    console.log('hide em');
  },

  blah: function() {
    console.log('blah');
  },

  // Render entire geocoding view.
  render: function () {

    var sidebarConainer = this.$el;
    _.each(controlViews, function(ControlView) {
      var view = new ControlView();
      sidebarConainer.append(view.render().el);
    });

    // TODO: move popover binding somewhere better
    this.popovers = this.$('.layer-info');
    this.bindPopovers();

    return this;
  },

  bindPopovers: function() {
    var popovers = this.popovers;

    // standard show/hide
    popovers.popover();

    // go away when the accordion container collapses
    $('.sidebar-tool').on('hide.bs.collapse', function() {
      popovers.popover('hide');
    });

    // hide all others when this cone comes up
    popovers.on('show.bs.popover', function() {
      var opening = this;
      popovers.each(function(i, triggerer) {
        if (triggerer !== opening) {
          $(triggerer).popover('hide');
        }
      });
    });
  }
});
