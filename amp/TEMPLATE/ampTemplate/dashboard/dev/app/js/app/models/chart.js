var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  defaults: {
    adjType: 'ac'
  },

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this, 'change:adjType', this.fetch);
  },

  fetch: function(options) {
    options = _.defaults(options || {}, { data: {adjType: this.get('adjType')} });
    return BackboneDash.Collection.prototype.fetch.call(this, options);
  }

});
