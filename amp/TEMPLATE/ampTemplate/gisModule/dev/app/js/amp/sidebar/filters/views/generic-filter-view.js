define(
  [
    'underscore',
    'backbone',
    'amp/sidebar/filters/views/base-filter-view',
    'amp/sidebar/filters/models/generic-filter-model',
    'text!amp/sidebar/filters/templates/generic-filter-template.html'
  ],
  function (_, Backbone, BaseFilterView, GenericFilterModel, Template) {
    'use strict';

    var View = BaseFilterView.extend({

      className: BaseFilterView.className + ' filter-org',
      template: _.template(Template),

      initialize: function() {
        BaseFilterView.prototype.initialize.apply(this);

        this.model = new GenericFilterModel();
      },

      renderFilters: function () {
        BaseFilterView.prototype.renderFilters.apply(this);
        this.$('.filter-options').append(this.template());
      }
    });

    return View;
  }
);
