var Backbone = require('backbone');

module.exports = Backbone.Model.extend({
  url: '/rest/security/user',

  defaults: {
    team: undefined
  },
  fetch: function(options) {
    options = options || {};
    options.cache = false;
    return Backbone.Model.prototype.fetch.call(this, options);
  }

});
