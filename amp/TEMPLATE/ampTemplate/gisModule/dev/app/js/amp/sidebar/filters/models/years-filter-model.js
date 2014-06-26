define(
  [
    'underscore',
    'backbone',
    'amp/sidebar/filters/models/base-filter-model'
  ],
  function (_, Backbone, BaseFilterModel) {
    'use strict';

    var Model = BaseFilterModel.extend({

      initialize: function() {
        BaseFilterModel.prototype.initialize.apply(this);
        this.set({
            title: 'Years',
            totalCount: 126,
            activeCount: 0
        });
      },

    });

    return Model;
  }
);
