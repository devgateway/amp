var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Boundary = require('../models/boundary-model');

/* Backbone Collection Boundaries (TODO rename file */
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({
  model: Boundary,
  //url: '/rest/gis/boundaries',
  // temporary so we get path to static files..
  url: function() {
    return '/TEMPLATE/ampTemplate/gisModule/dev/app/mock-api/data/boundaries/list.json';
  }
});
