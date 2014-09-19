var Backbone = require('backbone');
var $ = require('jquery');

  // Parent model for fitlers.
module.exports = Backbone.Model.extend({
  defaults: {
    title: 'Filter Name',
    totalCount: 375,
    activeCount: 375,
    _loaded: $.Deferred()
  },

  initialize:function(options) {
    this.set('title', options.title);
    this.url = options.url;
  }

});
