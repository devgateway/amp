var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var BaseFilterView = require('../views/base-filter-view');
var GenericFilterModel = require('../models/generic-filter-model');
var Template = fs.readFileSync(__dirname + '/../templates/generic-filter-template.html', 'utf8');


module.exports = BaseFilterView.extend({

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
