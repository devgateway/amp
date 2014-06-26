define(
  [
    'underscore',
    'backbone',
    'text!amp/map/templates/map-legend-template.html',
  ],
  function (_, Backbone, Template) {
    'use strict';

    var LegendView = Backbone.View.extend({

      template: _.template(Template),

      tagName: 'div',

      events: {

      },

      initialize: function () {
        _.bindAll(this, 'render');
      },

      render: function () {
        var self = this;
        this.$el.append(this.template());

        //TODO: chevron toggle: http://jsfiddle.net/zessx/R6EAW/12/

      }

    });

    return LegendView;
  }
);
