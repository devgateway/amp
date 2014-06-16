define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/tools/BaseTool/template.html",
    "text!" + APP_ROOT + "/amp/tools/Layers/templates/template.html"
  ],
  function (_, Backbone, BaseTemplate, Template) {
    'use strict';

    var LayersView = Backbone.View.extend({

      baseTemplate: _.template(BaseTemplate),
      template: _.template(Template),

      initialize: function () {

      },

      // Render entire geocoding view.
      render: function (title) {
          console.log('base tool render');

          //render base
          this.$el.html(this.baseTemplate({title:title}));

          // add content.
          this.$el.find('.content').html(this.template({title: title}));

      }
    });

    return LayersView;
  }
);
