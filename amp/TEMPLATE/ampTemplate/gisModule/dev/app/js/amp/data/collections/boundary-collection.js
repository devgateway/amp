var Backbone = require('backbone');
var Boundary = require('../models/boundary-model');

module.exports = Backbone.Collection.extend({
  url: '/rest/gis/boundaries',
  model: Boundary
});
