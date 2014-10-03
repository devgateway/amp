var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  initialize: function(options) {
    this.app = options.app;
  }

});
