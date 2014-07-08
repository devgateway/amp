var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var BaseFilterView = require('../views/base-filter-view');
var OrgFilterModel = require('../models/org-filter-model');
var Template = fs.readFileSync(path.join(__dirname, '../templates/org-filter-template.html'));


module.exports = BaseFilterView.extend({

  className: BaseFilterView.className + ' filter-org',
  template: _.template(Template),

  initialize: function() {
    BaseFilterView.prototype.initialize.apply(this);

    this.model = new OrgFilterModel();
  },

  renderFilters: function () {
    BaseFilterView.prototype.renderFilters.apply(this);
    this.$('.filter-options').append(this.template());
  }
});
