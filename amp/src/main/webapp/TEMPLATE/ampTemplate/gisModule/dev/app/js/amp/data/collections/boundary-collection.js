var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Boundary = require('../models/boundary-model');

/* Backbone Collection Boundaries (TODO rename file */
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({
  model: Boundary,
  url: function() {
    return '/rest/gis/boundaries';
  }
});
