define(
  [
    "underscore",
    "backbone",
    'js/amp/tools/Layers/views/LayersView.js',
    'js/amp/tools/Filters/views/FiltersView.js',
    'js/amp/tools/Search/views/SearchView.js',
    'js/amp/tools/Tools/views/ToolsView.js',
  ],
  function (_, Backbone, LayersView, FiltersView, SearchView, ToolsView) {
    'use strict';

    var SidebarToolsView = Backbone.View.extend({
      initialize: function () {

      },

      // Render entire geocoding view.
      render: function () {
        console.log('render tools');

        this.$el.append('<div id="tool-layers"></div>');
        var layerView = new LayersView({el:'#tool-layers'});
        layerView.render();

        this.$el.append('<div id="tool-search"></div>');
        var searchView = new SearchView({el:'#tool-search'});
        searchView.render();

        this.$el.append('<div id="tool-filters"></div>');
        var filtersView = new FiltersView({el:'#tool-filters'});
        filtersView.render();

        this.$el.append('<div id="tool-tools"></div>');
        var toolsView = new ToolsView({el:'#tool-tools'});
        toolsView.render(); 
      }
    });

    return SidebarToolsView;
  }
);
