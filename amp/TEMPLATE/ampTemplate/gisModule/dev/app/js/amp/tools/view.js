define(
  [
    "underscore",
    "backbone",
    'js/amp/tools/Layers/views/LayersView.js',
  ],
  function (_, Backbone, LayersView) {
    'use strict';

    var ToolsView = Backbone.View.extend({
      initialize: function () {

      },

      // Render entire geocoding view.
      render: function () {
        console.log('render tools');

        // TODO each will be each unique tool instantiated on its own.
        this.$el.append('<div id="tool-layers"></div>');
        var layerView = new LayersView({el:'#tool-layers'});
        layerView.render('Layers');

        this.$el.append('<div id="tool-tools"></div>');
        var toolsView = new LayersView({el:'#tool-tools'});
        toolsView.render('Tools');

        this.$el.append('<div id="tool-filters"></div>');
        var filtersView = new LayersView({el:'#tool-filters'});
        filtersView.render('Filters');
      }
    });

    return ToolsView;
  }
);
