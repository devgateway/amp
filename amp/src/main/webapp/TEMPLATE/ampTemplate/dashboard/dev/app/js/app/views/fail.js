var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/fail.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  initialize: function(options) {
    this.app = options.app;
    this.err = options.err;
  },

  render: function() {
    this.$el.html(template({ err: this.err }));
    return this;
  }

});

