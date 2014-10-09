var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/filters.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  className: 'row',

  events: {
    'click .show-filters': 'showFilters'
  },

  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    this.$el.html(template());
    this.app.filter.setElement(this.el.querySelector('#filter-popup'));
    return this;
  },

  showFilters: function() {
    this.app.filter.showFilters();
  }

});
