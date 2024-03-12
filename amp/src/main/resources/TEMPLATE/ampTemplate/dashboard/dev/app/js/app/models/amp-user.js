var Backbone = require('backbone');

module.exports = Backbone.Model.extend({
  url: '/rest/security/layout',

  /************
   * email is null from server when not logged in or when workspace not set yet.
   * Before it is fetched, default to undefined.
   */
  defaults: {
    email: undefined
  },
  fetch: function(options) {
    options = options || {};
    options.cache = false;
    return Backbone.Model.prototype.fetch.call(this, options);
  }


});
