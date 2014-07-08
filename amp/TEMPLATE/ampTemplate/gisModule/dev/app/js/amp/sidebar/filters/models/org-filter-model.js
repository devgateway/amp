var _ = require('underscore');
var Backbone = require('backbone');
var BaseFilterModel = require('../models/base-filter-model');


module.exports = BaseFilterModel.extend({

  initialize: function() {
    BaseFilterModel.prototype.initialize.apply(this);
    this.set({
        title: 'Organization',
        totalCount: 200,
        activeCount: 4
    });
  },

});
