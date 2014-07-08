var _ = require('underscore');
var Backbone = require('backbone');
var BaseFilterView = require('../views/base-filter-view');
var YearsFilterModel = require('../models/years-filter-model');


module.exports = BaseFilterView.extend({

  className: BaseFilterView.className + ' filter-years',

  initialize: function() {
    BaseFilterView.prototype.initialize.apply(this);

    this.model = new YearsFilterModel();
  },

});
