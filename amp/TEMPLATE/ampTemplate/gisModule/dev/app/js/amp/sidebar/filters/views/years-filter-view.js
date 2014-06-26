define(
  [
    'underscore',
    'backbone',
    'amp/sidebar/filters/views/base-filter-view',
    'amp/sidebar/filters/models/years-filter-model'
  ],
  function (_, Backbone, BaseFilterView, YearsFilterModel) {
    'use strict';

    var View = BaseFilterView.extend({

      className: BaseFilterView.className + ' filter-years',

      initialize: function() {
        BaseFilterView.prototype.initialize.apply(this);

        this.model = new YearsFilterModel();
      },

    });

    return View;
  }
);
