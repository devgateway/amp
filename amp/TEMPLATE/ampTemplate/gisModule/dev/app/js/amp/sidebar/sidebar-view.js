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

    var SidebarToolsView = Backbone.View.extend({
      initialize: function () {

      },

      // Render entire geocoding view.
      render: function () {

        this.$el.append('<div id="tool-layers"  class="panel sidebar-tool"></div>');
        var layerView = new LayersView({el:'#tool-layers'});
        layerView.render();

        this.$el.append('<div id="tool-search" class="panel sidebar-tool"></div>');
        var searchView = new SearchView({el:'#tool-search'});
        searchView.render();

        this.$el.append('<div id="tool-filters" class="panel sidebar-tool"></div>');
        var filtersView = new FiltersView({el:'#tool-filters'});
        filtersView.render();

        this.$el.append('<div id="tool-tools" class="panel sidebar-tool"></div>');
        var toolsView = new ToolsView({el:'#tool-tools'});
        toolsView.render();

        this.$el.append('<div id="tool-settings" class="panel sidebar-tool"></div>');
        var settingsView = new SettingsView({el:'#tool-settings'});
        settingsView.render();

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
