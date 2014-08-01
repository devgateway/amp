var Backbone = require('backbone');
var APIHelper = require('../../../libs/local/api-helper');
var Indicator = require('../models/indicator-model');


module.exports = Backbone.Collection.extend({

  url: APIHelper.getAPIBase() + '/rest/gis/indicator-layers',

  model: Indicator,

  initialize: function(options) {
    this.boundaries = options.boundaries;
  },


// phil: these commands will be common to the project layer collection, and potentially other future collections,
// should we abstract into a base class?
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

// phil, f.y.i this can also be a quick way of setting a property in all models in a collection: this.invoke('set', {'selected',false});
// not sure how well it will work with unset..
  clearSelected: function() {
    this.getSelected().each(function(layer) {
      layer.unset('selected');
    });
  }

});
