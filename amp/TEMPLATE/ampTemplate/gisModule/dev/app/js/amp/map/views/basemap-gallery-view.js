/**
 * This view wraps the ESRI base map gallery component
 */
define(
  [
    'underscore',
    'backbone',
    'esri/dijit/BasemapGallery',
    'text!amp/map/templates/basemap-gallery-template.html',
  ],
  function (_, Backbone, BasemapGallery, Template) {
    'use strict';

    var BasemapView = Backbone.View.extend({

      template: _.template(Template),

      tagName: 'ul',
      id: 'basemap-gallery',
      className: 'nav navbar-nav navbar-right dropdown',

      initialize: function () {
        _.bindAll(this, 'render');
      },

      render: function () {
        this.$el.html(this.template());

        this.basemapGallery = this.basemapGallery || new BasemapGallery({
          showArcGISBasemaps: true,
          map: this.map
        }, 'basemapGallery');

        this.basemapGallery.startup();

        return this;
      }

    });

    return BasemapView;
  }
);
