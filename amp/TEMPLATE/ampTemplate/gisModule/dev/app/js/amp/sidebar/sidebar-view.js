define(
  [
    'underscore',
    'backbone',
    'js/amp/sidebar/layers/views/layers-view.js',
    'js/amp/sidebar/filters/views/filters-view.js',
    'js/amp/sidebar/search/views/search-view.js',
    'js/amp/sidebar/tools/views/tools-view.js',
    'js/amp/sidebar/source-categories/views/source-categories-view.js',
    'js/amp/sidebar/data-sources/views/data-sources-view.js',
  ],
  function (_, Backbone, LayersView, FiltersView, SearchView, ToolsView, SourceCategoriesView, DataSourcesView) {
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

        this.$el.append('<div id="tool-sources-category" class="panel sidebar-tool"></div>');
        var sourceCategoriesView = new SourceCategoriesView({el:'#tool-sources-category'});
        sourceCategoriesView.render();

        this.$el.append('<div id="tool-data-sources" class="panel sidebar-tool"></div>');
        var dataSourcesView = new DataSourcesView({el:'#tool-data-sources'});
        dataSourcesView.render();
      }
    });

    return SidebarToolsView;
  }
);
