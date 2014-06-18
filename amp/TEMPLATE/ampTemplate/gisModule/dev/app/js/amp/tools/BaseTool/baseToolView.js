define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/tools/BaseTool/template.html",
  ],
  function (_, Backbone, BaseTemplate, Template) {
    'use strict';

    var BaseToolView = Backbone.View.extend({

      defaults: {
        title: "",
        iconClass: "",
        description: ""
      },

      initialize: function () {

        this.baseTemplate = _.template(BaseTemplate);
       
      },

      render: function() {
        // render base
        this.$el.html(this.baseTemplate({
          title: this.title,
          titleID: this.title.replace(/ /g, '').toLowerCase(),
          iconClass: this.iconClass,
          description: this.description
        }));
      }

    });

    return BaseToolView;
  }
);
