var Backbone = require('backbone');
var APIHelper = require('../../../libs/local/api-helper');
var Indicator = require('../models/indicator-model');


module.exports = Backbone.Collection.extend({

  url: APIHelper.getAPIBase() + '/rest/gis/indicator-layers/',

  model: Indicator,

  initialize: function(options) {
    this.boundaries = options.boundaries;
  },

  select: function(indicator) {
    this.clearSelected();
    indicator.set('selected', true);
  },

  unselect: function(indicator) {
    indicator.unset('selected');
  },

  getSelected: function() {
    return this.chain()
      .filter(function(layer) { return layer.get('selected'); });
  },

  clearSelected: function() {
    this.getSelected().each(function(layer) {
      layer.unset('selected');
    });
  }

});
