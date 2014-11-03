var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var Filters = require('./filters');
var Share = require('./share');


module.exports = BackboneDash.View.extend({

  className: 'row',

  initialize: function(options) {
    this.app = options.app;
    this.filters = new Filters({ app: this.app });
    this.share = new Share({ app: this.app });
  },

  render: function() {
    this.$el.html([
      this.filters.render().el,
      this.share.render().el
    ]);
    return this;
  }

});
