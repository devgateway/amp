var BackboneDash = require('../backbone-dash');
var Chart = require('./chart');


module.exports = BackboneDash.Collection.extend({

  model: Chart,

  initialize: function(options) {
    this.app = options.app;
  }

});
