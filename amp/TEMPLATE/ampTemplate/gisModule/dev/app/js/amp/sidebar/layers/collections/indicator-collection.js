var _ = require('underscore');
var Backbone = require('backbone');
var IndicatorModel = require('../models/indicator-model');

var APIBase = require('../../../../libs/local/api-base');

module.exports = Backbone.Collection.extend({
  model: IndicatorModel,
  url: APIBase.getAPIBase() + '/rest/gis/indicator-layers',

  initialize: function() {

  }

});
