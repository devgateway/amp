var BaseFilterModel = require('../models/base-filter-model');


module.exports = BaseFilterModel.extend({

  initialize: function() {
    BaseFilterModel.prototype.initialize.apply(this);
    this.set({
        title: 'Years',
        totalCount: 126,
        activeCount: 0
      });
  }

});
