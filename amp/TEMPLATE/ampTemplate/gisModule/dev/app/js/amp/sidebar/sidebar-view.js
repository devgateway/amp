define(
  [
    'jquery',
    'underscore',
    'backbone',
    'amp/sidebar/layers/views/layers-view',
    'amp/sidebar/filters/views/filters-view',
    'amp/sidebar/search/views/search-view',
    'amp/sidebar/tools/views/tools-view',
    'amp/sidebar/settings/views/settings-view',
  ],
  function ($, _, Backbone, LayersView, FiltersView, SearchView, ToolsView, SettingsView) {
    'use strict';

    var controlViews = [
      LayersView,
      SearchView,
      FiltersView,
      ToolsView,
      SettingsView
    ];

    var SidebarToolsView = Backbone.View.extend({
      initialize: function () {

      },

      // Render entire geocoding view.
      render: function () {

        var sidebarConainer = this.$el;
        _.each(controlViews, function(ControlView) {
          var view = new ControlView();
          sidebarConainer.append(view.render().el);
        });

        return this;

        // TODO: where does this jquery behavioural stuff go?
        // bind all the popovers
        var popovers = $('.layer-info');
        popovers.popover();
        // hide popovers when the drawer closes
        $('.sidebar-tool').on('hide.bs.collapse', function() {
          popovers.popover('hide');
        });
        popovers.on('show.bs.popover', function() {
          // hide other open popovers
          var opening = this;
          popovers.each(function(i, triggerer) {
            if (triggerer !== opening) {
              $(triggerer).popover('hide');
            }
          });
        });
      }
    });

    return SidebarToolsView;
  }
);
