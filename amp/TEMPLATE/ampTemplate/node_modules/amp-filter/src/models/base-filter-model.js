var Backbone = require('backbone');

  // Parent model for fitlers.
module.exports = Backbone.Model.extend({
  defaults: {
    title: 'Filter Name',
    method: 'GET',
    totalCount: 375,
    activeCount: 375,
    _loaded: false
  },

  initialize:function(options) {
    this.set('title', options.title || options.name);
    this.set('group', options.group || options.name);
    this.set('method', options.method);
    this.url =  options.url || options.endpoint;
  }

});
