var Backbone = require('backbone');

module.exports = Backbone.Model.extend({
  url: '/rest/security/user',

  /************
   * email is null from server when not logged in or when workspace not set yet.
   * Before it is fetched, default to undefined.
   */
  defaults: {
    email: undefined
  }


});
