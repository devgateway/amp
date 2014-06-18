define(
  [
    'underscore',
    'backbone',
    'text!' + APP_ROOT + '/amp/dataquality/templates/dataquality-template.html'
  ],
  function (_, Backbone, Template) {
    'use strict';

    var DataQuality = Backbone.View.extend({

      template: _.template(Template),

      initialize: function () {

      },

      render: function () {
        console.log('dataquality render');
        this.$el.html(this.template({}));
        console.log('dataquality render done');
      }
    });

    return DataQuality;
  }
);
