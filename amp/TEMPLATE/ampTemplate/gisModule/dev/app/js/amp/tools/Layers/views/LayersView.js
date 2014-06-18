define(
  [
    "underscore",
    "backbone",
    APP_ROOT + "/amp/tools/BaseTool/baseToolView.js",
    "text!" + APP_ROOT + "/amp/tools/Layers/templates/template.html"
  ],
  function (_, Backbone, BaseToolView, Template) {
    'use strict';

    var LayersView = BaseToolView.extend({

      initialize: function() {
        BaseToolView.prototype.initialize.apply(this);
        this.template = _.template(Template);
        this.title = "Layers";
        this.iconClass = "ampicon-layers";
        this.description = "Tool desc, remove if possible.";
      }
    });

    return LayersView;
  }
);
