var Backbone = require('backbone');
var LocalStorage = require('backbone.localstorage');

module.exports = Backbone.Model.extend({
  url: '/rest/security/menus',
  localStorage: new LocalStorage('amp-menus'),

  defaults: {
    name: "Default",
    children: []
  },
  fetch: function(options) {
    options = options || {};
    options.cache = false;
    return Backbone.Model.prototype.fetch.call(this, options);
  }

});
