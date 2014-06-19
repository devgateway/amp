define(
  [
    'underscore',
    'backbone',
    APP_ROOT + '/amp/sidebar/base-control/base-control-view.js',
    'text!' + APP_ROOT + '/amp/sidebar/filters/templates/filter-template.html'
  ],
  function (_, Backbone, BaseToolView, Template) {
    'use strict';

    var View = BaseToolView.extend({

      title: 'Filters',
      iconClass: 'ampicon-filters',
      description: 'Apply filters to the map.',

      template: _.template(Template),

      initialize: function() {
        BaseToolView.prototype.initialize.apply(this);

        //TODO: Create all the custom views we need, and push them into filterViews:
        //      These views should inherit from a parent view.
        //        organizationsFilter
        //        locationsFilter
        //        yearsFilter
        //        etc...


        //TODO: register listener for FILTER_CHANGED event, then iterate over
        //      filterViews and call createFilterJSON on each model
        //      create master filter object and pass it to the map. to call api and re-render.

        // add content
        this.$('.content').html(this.template({title: this.title}));
      },

      //Intentionally not a 'collection' because it won't be populated by the API, and it holds views not models.
      filterViews:[],

      render: function() {
        BaseToolView.prototype.render.apply(this);

        // add content
        this.$('.content').html(this.template({title: this.title}));

        // Sample showing how to set checkbox to 'intermediate' state.
        this.$('.filter-type input:checkbox').prop('indeterminate', true);

        // TODO -- render: Iterate over filterViews and render them all.
      }
    });

    return View;
  }
);
