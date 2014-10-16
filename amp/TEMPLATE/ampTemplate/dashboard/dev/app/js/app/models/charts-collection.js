var BackboneDash = require('../backbone-dash');
var Chart = require('./chart');


module.exports = BackboneDash.Collection.extend({

  url: '/rest/dashboard/tops',

  model: Chart,

  initialize: function(options) {
    this.app = options.app;
  }

});
