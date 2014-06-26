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

      className: BaseFilterView.className + ' filter-org',
      template: _.template(Template),

      initialize: function() {
        BaseFilterView.prototype.initialize.apply(this);

        this.model = new OrgFilterModel();
      },

    });

    return View;
  }
);
