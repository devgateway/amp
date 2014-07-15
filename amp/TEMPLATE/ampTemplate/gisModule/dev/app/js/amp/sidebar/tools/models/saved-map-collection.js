var _ = require('underscore');
var Backbone = require('backbone');

var SavedMapModel = require('./saved-map-model');


module.exports = Backbone.Collection.extend({
  url: '/states/load',
  fetch: function() {
    console.log('would fetch saved maps here...');  // faking it out for now
  },

  model: SavedMapModel
});


