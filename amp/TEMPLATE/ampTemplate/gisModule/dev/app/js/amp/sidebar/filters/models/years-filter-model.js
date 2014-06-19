define(
  [
    'underscore',
    'backbone',
    APP_ROOT + '/amp/sidebar/filters/models/base-filter-model.js',
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

