var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var ProjectsView = require('./layers/views/projects-view');
var IndicatorLayersView = require('./layers/views/indicator-layers-view');
var FiltersView = require('./filters/views/sidebar-filters-view');
//var SearchView = require('./search/views/search-view');
var ToolsView = require('./tools/views/tools-view');
var SettingsView = require('./settings/views/settings-view');


var controlViews = [
  ProjectsView,
  IndicatorLayersView,
  FiltersView,
//  SearchView, //disabled for 2.10
  ToolsView,
  SettingsView
];

module.exports = Backbone.View.extend({

  events: {
    'hide.bs.collapse .sidebar-tool': 'hideAllPopovers',
    'show.bs.popover .layer-info': 'exclusiveShowPopover'
  },

  initialize: function(options) {
    this.app = options.app;
  },

  // Render entire geocoding view.
  render: function() {
    this.$el.append(_.map(controlViews, function(ControlView) {
      var view = new ControlView({app: this.app});
      return view.render().el;
    }));

    // sidebar popover init
    this.popovers = this.$('.layer-info');
    this.popovers.popover();

    // Temp dirty popover fix until 'exclusiveShowPopover' is fixed in next sprint.
    $('body').on('click', function(e) {
      if ($(e.target).data('toggle') !== 'popover' &&
          $(e.target).parents('[data-toggle="popover"]').length === 0 &&
          $(e.target).parents('.popover.in').length === 0) {
        $('[data-toggle=popover]').popover('hide');
      }
    });

    return this;
  },

  hideAllPopovers: function() {
    this.popovers.popover('hide');
  },

//TODO: doesn't work, opening is a Backbone 'view', triggerer is a DOM element
  exclusiveShowPopover: function() {
    /*
    var opening = this;
    this.popovers.each(function(i, triggerer) {
      if (triggerer !== opening) {
        $(triggerer).popover('hide');
      }
    });*/
  }

});
