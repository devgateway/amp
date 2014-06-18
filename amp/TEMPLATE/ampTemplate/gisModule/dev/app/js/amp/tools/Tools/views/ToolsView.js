define(
  [
    "underscore",
    "backbone",
    APP_ROOT + "/amp/tools/BaseTool/baseToolView.js",
    "text!" + APP_ROOT + "/amp/tools/Tools/templates/template.html"
  ],
  function (_, Backbone, BaseToolView, Template) {
    'use strict';

    var View = BaseToolView.extend({

      initialize: function() {
        BaseToolView.prototype.initialize.apply(this);
        this.template = _.template(Template);
        this.title = "Tools";
        this.iconClass = "ampicon-tools";
        this.description = "Various tools";
      }
    });

    return View;
  }
);
