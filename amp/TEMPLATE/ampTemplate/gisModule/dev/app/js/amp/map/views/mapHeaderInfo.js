/**
 * Pertinent details contextualizing the data on the map.
 */
define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/map/templates/map-header-info.html",
  ],
  function (_, Backbone, Template) {
    'use strict';

    var BasemapView = Backbone.View.extend({

      template: _.template(Template),

      tagName: 'span',

      events: {

      },

      initialize: function () {
        _.bindAll(this, "render");
      },

      render: function () {
        var self = this;
        this.$el.append(this.template()).addClass('navbar-brand');
        return this.$el;
      }

    });

    return BasemapView;
  }
);
