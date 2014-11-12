var BackboneDash = require('../backbone-dash');
var boilerplate = require('amp-boilerplate');


module.exports = BackboneDash.View.extend({

  className: 'row',

  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    this.$el.html(boilerplate.footer);
    return this;
  }

});
