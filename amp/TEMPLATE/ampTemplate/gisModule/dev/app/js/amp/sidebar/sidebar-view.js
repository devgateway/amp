var $ = require('jquery');
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
    'hide.bs.collapse .sidebar-tool': 'hideAllPopovers',
    'show.bs.popover .layer-info': 'exclusiveShowPopover'
  },

  // Render entire geocoding view.
  render: function () {
    this.$el.append(_.map(controlViews, function(ControlView) {
      var view = new ControlView();
      return view.render().el;
    }));

    // sidebar popover init
    this.popovers = this.$('.layer-info');
    this.popovers.popover();

    return this;
  },

  hideAllPopovers: function() {
    this.popovers.popover('hide');
  },

  exclusiveShowPopover: function() {
    var opening = this;
    this.popovers.each(function(i, triggerer) {
      if (triggerer !== opening) {
        $(triggerer).popover('hide');
      }
    });
  }

});
