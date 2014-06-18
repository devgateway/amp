define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/tools/BaseTool/template.html",
  ],
  function (_, Backbone, BaseTemplate, Template) {
    'use strict';

    var BaseToolView = Backbone.View.extend({

      initialize: function () {
        this.baseTemplate = _.template(BaseTemplate);

        this.render = function() {
          // render base
          this.$el.html(this.baseTemplate({
            title: this.title,
            titleID: this.title.replace(/ /g, '').toLowerCase(),
            iconClass: this.iconClass,
            description: this.description
          }));

          // add content
          this.$el.find('.content').html(this.template({title: this.title}));
        };
      }
    });

    return BaseToolView;
  }
);
