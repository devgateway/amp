var Backbone = require('backbone');

  // Parent model for fitlers.
module.exports = Backbone.Model.extend({
  defaults: {
    title: 'Filter Name',
    totalCount: 375,
    activeCount: 375
  },

  initialize: function() {

  }

});
