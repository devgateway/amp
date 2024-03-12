var Backbone = require('backbone');
var MenuModel = require('../models/amp-menus-model.js');

module.exports = Backbone.Collection.extend({
  url: '/rest/security/menus',
  model: MenuModel,

  fetch: function(options) {
    options = options || {};
    options.cache = false;
    return Backbone.Model.prototype.fetch.call(this, options);
  }

});
