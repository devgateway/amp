var _ = require('underscore');
var Backbone = require('backbone');
var IndicatorModel = require('../models/indicator-model');

var APIHelper = require('../../../../libs/local/api-helper');

module.exports = Backbone.Collection.extend({
  model: IndicatorModel,
  url: APIHelper.getAPIBase() + '/rest/gis/indicator-layers',

  initialize: function() {

  }

});
