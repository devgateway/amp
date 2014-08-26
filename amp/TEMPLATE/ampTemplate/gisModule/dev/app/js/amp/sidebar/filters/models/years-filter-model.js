var BaseFilterModel = require('../models/base-filter-model');


module.exports = BaseFilterModel.extend({

  initialize: function() {
    BaseFilterModel.prototype.initialize.apply(this);
    this.set({
        title: 'Years',
        start: 1990,
        end: 2014,
        min:1960,
        max:2015
      });
  }

});
