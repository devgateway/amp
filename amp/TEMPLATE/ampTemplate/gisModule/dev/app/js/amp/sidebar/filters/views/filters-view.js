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

    var View = BaseToolView.extend({
      title: 'Filters',
      iconClass: 'ampicon-filters',
      description: 'Apply filters to the map.',

      //Intentionally not a 'collection' because it won't be populated by the API, and it holds views not models.
      filterViews:[],

      template: _.template(Template),

      initialize: function() {
        BaseToolView.prototype.initialize.apply(this);

        //TODO: register listener for FILTER_CHANGED event, then iterate over
        //      filterViews and call createFilterJSON on each model
        //      create master filter object and pass it to the map. to call api and re-render.

        // add content
        this.$('.content').html(this.template({title: this.title}));
      },


      render: function() {
        BaseToolView.prototype.render.apply(this);

        // add content
        this.$('.content').html(this.template({title: this.title}));

        this.$('.filter-list').append('<div class="filter-type filter-org"></div>');
        var orgFilterView = new OrgFilterView({el: '.filter-org'});
        this.filterViews.push(orgFilterView);

        this.$('.filter-list').append('<div class="filter-type filter-years"></div>');
        var yearsFilterView = new YearsFilterView({el: '.filter-years'});
        this.filterViews.push(yearsFilterView);


        // TODO -- render: Iterate over filterViews and render them all.
        orgFilterView.renderTitle();
        yearsFilterView.renderTitle();
      }
    });

    return View;
  }
);
