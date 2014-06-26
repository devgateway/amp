define(
  [
    'underscore',
    'backbone',
    'amp/sidebar/filters/views/base-filter-view',
    'amp/sidebar/filters/models/org-filter-model',
    'text!amp/sidebar/filters/templates/org-filter-template.html'
  ],
  function (_, Backbone, BaseFilterView, OrgFilterModel, Template) {
    'use strict';

    var View = BaseFilterView.extend({

      template: _.template(Template),

      initialize: function() {
        BaseFilterView.prototype.initialize.apply(this);

        this.model = new OrgFilterModel();
      },

      renderTitle: function() {
        BaseFilterView.prototype.renderTitle.apply(this);


      },

      // Called when user clicks on a title.
      renderContent: function() {
        BaseFilterView.prototype.renderContent.apply(this);


      },

    });

    return View;
  }
);
