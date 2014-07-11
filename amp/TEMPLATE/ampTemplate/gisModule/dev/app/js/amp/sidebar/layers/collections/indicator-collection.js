var _ = require('underscore');
var Backbone = require('backbone');
var IndicatorModel = require('../models/indicator-model');


module.exports = Backbone.Collection.extend({
  model: IndicatorModel,
  url: '/js/mock-api/indicators.json',

  initialize: function() {
    
  }

});
