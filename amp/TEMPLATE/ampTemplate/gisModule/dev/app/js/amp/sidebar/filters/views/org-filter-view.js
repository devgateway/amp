var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var BaseFilterView = require('../views/base-filter-view');
var OrgFilterModel = require('../models/org-filter-model');
var Template = fs.readFileSync(__dirname + '/../templates/org-filter-template.html', 'utf8');


module.exports = BaseFilterView.extend({

  className: BaseFilterView.className + ' filter-org',
  template: _.template(Template),

  initialize: function(options) {
    BaseFilterView.prototype.initialize.apply(this);

    this.model = new OrgFilterModel(options.modelValues);
  },

  renderFilters: function () {
    BaseFilterView.prototype.renderFilters.apply(this);
    this.$('.filter-options').append(this.template());
  }
});
