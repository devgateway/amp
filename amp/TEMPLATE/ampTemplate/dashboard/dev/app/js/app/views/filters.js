var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/filters.html', 'UTF-8'));
var summary_template = _.template(fs.readFileSync(
  __dirname + '/../templates/filter-summary.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  events: {
    'click .show-filters': 'showFilter'
  },

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.app.filter, 'cancel', this.hideFilter);
    this.listenTo(this.app.filter, 'apply', this.applyFilter);

    this.app.filter.loaded.then(_(function() {
      this.app.state.register(this, 'filters', {
        get: _(this.app.filter.serialize).bind(this.app.filter),
        set: _(this.app.filter.deserialize).bind(this.app.filter),
        empty: {}
      });
    }).bind(this));
  },

  render: function() {
    this.$el.html(template());
    this.app.filter.setElement(this.el.querySelector('#filter-popup'));
    this.hideFilter();
    this.app.filter.loaded
      .done(_(this.renderApplied).bind(this))
      .fail(_(function(a1, a2, message) {
        this.$('.applied-filters').html('<strong class="text-danger filters-err">Failed to load filters</strong> <a href="" class="btn btn-warning btn-sm"><span class="glyphicon glyphicon-refresh"></span> Refresh page</a>');
        this.$('button').addClass('disabled');
      }).bind(this));
    return this;
  },

  renderApplied: function() {
    var filters = this.app.filter.serializeToModels();
    var applied = _(filters.columnFilters).keys();
    if (filters.otherFilters) {
      // Currently assumes that any otherFilters just implies Date Range
      // ... there is no obvious way to get nice strings out.
      applied.push('Date range');
    }
    applied = applied.join(', ');
    this.$('.applied-filters').html(summary_template({ applied: applied }));
  },

  showFilter: function() {
    this.app.filter.showFilters();
    this.$('#filter-popup').show();
  },

  hideFilter: function() {
    this.$('#filter-popup').hide();
  },

  applyFilter: function() {
    // todo: actually do an effect for changed filters...
    this.hideFilter();
    this.renderApplied();
  }

});
