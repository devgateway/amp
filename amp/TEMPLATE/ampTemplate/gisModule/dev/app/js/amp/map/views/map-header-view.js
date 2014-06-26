/**
 * Pertinent details contextualizing the data on the map.
 */
define(
  [
    'underscore',
    'backbone',
    'text!amp/map/templates/map-header-info-template.html',
  ],
  function (_, Backbone, Template) {
    'use strict';

    var View = Backbone.View.extend({

      className: 'map-header navbar-header navbar-brand',

      template: _.template(Template),

      render: function () {
        this.$el.html(this.template());
        return this;
      }

    });

    return View;
  }
);
