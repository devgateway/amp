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

      id: 'legend',

      render: function () {
        this.$el.html(this.template());
        return this;

        //TODO: chevron toggle: http://jsfiddle.net/zessx/R6EAW/12/
      }

    });

    return LegendView;
  }
);
