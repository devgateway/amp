// Is this view used at all?

define(
  [
    "underscore",
    "backbone",
    'js/amp/tools/Filters/models/Model.js'
  ],
  function (_, Backbone, FilterModel) {
    'use strict';

    var View = Backbone.View.extend({

      baseTemplate: _.template(BaseTemplate),
      template: _.template(Template),

      initialize: function () {
        this.model = new FilterModel();
      },

      render: function () {

      }
    });

    return View;
  }
);
