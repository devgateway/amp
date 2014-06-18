/**
 * This view wraps the ESRI base map gallery component
 */
define(
  [
    'underscore',
    'backbone',
    'text!' + APP_ROOT + '/amp/map/templates/basemap-gallery-template.html',
  ],
  function (_, Backbone, Template) {
    'use strict';

    var BasemapView = Backbone.View.extend({

      template: _.template(Template),

      tagName: 'li',

      events: {

      },

      initialize: function () {
        _.bindAll(this, 'render');
      },

      render: function () {
        var self = this;
        this.$el.append(this.template()).addClass('drop-down');
        require(['esri/dijit/BasemapGallery', 'esri/arcgis/utils', 'dojo/parser', 'dijit/layout/BorderContainer', 'dijit/layout/ContentPane', 'dijit/TitlePane'], function (BasemapGallery, arcgisUtils, parser) {

          var basemapGallery = new BasemapGallery({
            showArcGISBasemaps: true,
            map: self.options.map
          }, 'basemapGallery');

          basemapGallery.startup();
        });

        return this.$el;
      }

    });

    return BasemapView;
  }
);
