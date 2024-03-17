var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Collection.extend({
  initialize: function(models, options) {
    this.app = options.app;
  }
});
