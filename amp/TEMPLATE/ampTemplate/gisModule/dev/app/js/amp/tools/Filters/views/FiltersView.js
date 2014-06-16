define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/tools/BaseTool/template.html",
    "text!" + APP_ROOT + "/amp/tools/Filters/templates/template.html"
  ],
  function (_, Backbone, BaseTemplate, Template) {
    'use strict';

    var LayersView = Backbone.View.extend({

      baseTemplate: _.template(BaseTemplate),
      template: _.template(Template),
      title: "Filters",
      iconClass: "ampicon-filters",
      description: "Apply filters to the map.",

      initialize: function () {

      },

      // Render entire geocoding view.
      render: function () {
          console.log('base tool render');

          //render base
          // TODO: abstract some of this into a new BaseTemplate view:
          this.$el.html(this.baseTemplate({
            title: this.title, 
            titleID:this.title.replace(/ /g,''), 
            iconClass: this.iconClass,
            description: this.description
          }));

          // add content.
          this.$el.find('.content').html(this.template({title: this.title}));

      }
    });

    return LayersView;
  }
);
