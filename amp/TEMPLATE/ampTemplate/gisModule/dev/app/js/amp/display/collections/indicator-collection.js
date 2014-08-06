var _ = require('underscore');
var Backbone = require('backbone');
var IndicatorLayer = require('../models/indicator-model');


module.exports = Backbone.Collection.extend({

  model: IndicatorLayer,

  initialize: function(models, options) {
    this.data = options.data;
    this.display = options.display;

    this.listenTo(this.data.indicators, 'add', this.addIndicator);
  },

  addIndicator: function(indicator) {
    this.add({
      data: this.data,
      display: this.display,
      indicator: indicator
    });
  }

});
