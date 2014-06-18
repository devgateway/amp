define(
  [
    "underscore",
    "backbone",
    APP_ROOT + "/amp/tools/BaseTool/baseToolView.js",
    "text!" + APP_ROOT + "/amp/tools/Search/templates/template.html"
  ],
  function (_, Backbone, BaseToolView, Template) {
    'use strict';

    var View = BaseToolView.extend({

      initialize: function() {
        BaseToolView.prototype.initialize.apply(this);
        this.template = _.template(Template);
        this.title = "Keyword Search";
        this.iconClass = "ampicon-search";
        this.description = "";
      }
    });

    return View;
  }
);


