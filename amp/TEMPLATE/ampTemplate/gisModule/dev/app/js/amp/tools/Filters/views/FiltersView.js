define(
  [
    "underscore",
    "backbone",
    "text!" + APP_ROOT + "/amp/tools/BaseTool/template.html",
    "text!" + APP_ROOT + "/amp/tools/Filters/templates/template.html",
    'js/amp/tools/Filters/models/Filter.js'
  ],
  function (_, Backbone, BaseTemplate, Template, FilterModel) {
    'use strict';

    var View = Backbone.View.extend({

      baseTemplate: _.template(BaseTemplate),
      template: _.template(Template),
      title: "Filters",
      iconClass: "ampicon-filters",
      description: "Apply filters to the map.",
 
      //Intentionally not a 'collection' because it won't be populated by the API, and it holds views not models.
      filterViews:[],

      initialize: function () {

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

      // Render entire geocoding view.
      render: function () {

          //render base
          this.$el.html(this.baseTemplate({
            title: this.title, 
            titleID:this.title.replace(/ /g,''), 
            iconClass: this.iconClass,
            description: this.description
          }));

          // add content.
          this.$el.find('.content').html(this.template({title: this.title}));

          // TODO: Iterate over filterViews and render them all.

      }
    });

    return View;
  }
);
