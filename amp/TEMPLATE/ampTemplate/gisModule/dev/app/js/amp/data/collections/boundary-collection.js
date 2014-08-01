var Backbone = require('backbone');
var APIHelper = require('../../../libs/local/api-helper');
var Boundary = require('../models/boundary-model');

module.exports = Backbone.Collection.extend({
  url: APIHelper.getAPIBase() + '/rest/gis/boundaries/',
  model: Boundary
});
