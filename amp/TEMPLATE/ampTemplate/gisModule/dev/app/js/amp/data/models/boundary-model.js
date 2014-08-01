var Backbone = require('backbone');

module.exports = Backbone.Model.extend({
  initialize: function() {
    this.fetch();  // TODO: lazy-load boundaries as needed
  }
});
