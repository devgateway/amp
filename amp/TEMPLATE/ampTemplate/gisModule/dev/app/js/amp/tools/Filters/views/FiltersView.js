define(
  [
    "underscore",
    "backbone",
    APP_ROOT + "/amp/tools/BaseTool/baseToolView.js",
    "text!" + APP_ROOT + "/amp/tools/Filters/templates/template.html"
  ],
  function (_, Backbone, BaseToolView, Template) {
    'use strict';

    var View = BaseToolView.extend({

      initialize: function() {
        BaseToolView.prototype.initialize.apply(this);
        this.template = _.template(Template);
        this.title = "Filters";
        this.iconClass = "ampicon-filters";
        this.description = "Apply filters to the map.";

        //TODO: Create all the custom views we need, and push them into filterViews:
        //      These views should inherit from a parent view.
        //        organizationsFilter
        //        locationsFilter
        //        yearsFilter
        //        etc...


        //TODO: register listener for FILTER_CHANGED event, then iterate over
        //      filterViews and call createFilterJSON on each model
        //      create master filter object and pass it to the map. to call api and re-render.
      },

      //Intentionally not a 'collection' because it won't be populated by the API, and it holds views not models.
      filterViews:[],

      // TODO -- render: Iterate over filterViews and render them all.
    });

    return View;
  }
);
