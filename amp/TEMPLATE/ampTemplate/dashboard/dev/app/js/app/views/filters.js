var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/filters.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  className: 'row',

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
    return this;
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
  }

});
