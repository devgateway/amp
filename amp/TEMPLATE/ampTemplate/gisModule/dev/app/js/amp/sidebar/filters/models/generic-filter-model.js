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

        // this.set({
        //     title: 'Organization',
        //     totalCount: 200,
        //     activeCount: 4
        // });
        
      },

    });

    return Model;
  }
);

