define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/tools/BaseTool/template.html",
    "text!" + APP_ROOT + "/amp/tools/Search/templates/template.html"
  ],
  function (_, Backbone, BaseTemplate, Template) {
    'use strict';

    var LayersView = Backbone.View.extend({

      baseTemplate: _.template(BaseTemplate),
      template: _.template(Template),
      title: "Keyword Search",
      iconClass: "ampicon-search",
      description: "",

      initialize: function () {

      },

      // Render entire geocoding view.
      render: function () {

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
