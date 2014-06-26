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

    var BasemapView = Backbone.View.extend({

      template: _.template(Template),

      tagName: 'span',

      events: {

      },

      initialize: function () {
        _.bindAll(this, 'render');
      },

      render: function () {
        var self = this;
        this.$el.append(this.template());
        return this.$el;
      }

    });

    return BasemapView;
  }
);
