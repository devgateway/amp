define(
  [
    'underscore',
    'backbone',
    'amp/sidebar/base-control/base-control-view',
    'amp/sidebar/filters/views/org-filter-view',
    'amp/sidebar/filters/views/years-filter-view',
    'text!amp/sidebar/filters/templates/filters-template.html'
  ],
  function (_, Backbone, BaseToolView, OrgFilterView, YearsFilterView, Template) {
    'use strict';

    var filterViews = [OrgFilterView, YearsFilterView];

    var View = BaseToolView.extend({
      id: 'tool-filters',
      title: 'Filters',
      iconClass: 'ampicon-filters',
      description: 'Apply filters to the map.',

      //Intentionally not a 'collection' because it won't be populated by the API, and it holds views not models.
      filterViewsInstances:[],

      template: _.template(Template),

      initialize: function() {
        BaseToolView.prototype.initialize.apply(this);

        //TODO: register listener for FILTER_CHANGED event, then iterate over
        //      filterViews and call createFilterJSON on each model
        //      create master filter object and pass it to the map. to call api and re-render.

        var filtersContainer = this;
        _.each(filterViews, function(FilterView) {
          var view = new FilterView();
          filtersContainer.filterViewsInstances.push(view);
        });
      },

      render: function() {
        BaseToolView.prototype.render.apply(this);

        // add content
        this.$('.content').html(this.template({title: this.title}));

        var filtersContainer = this;
        _.each(this.filterViewsInstances, function(filterView) {
          filtersContainer.$('.filter-list').append(filterView.renderTitle().el);
        });

        return this;
      }
    });

    return View;
  }
);
