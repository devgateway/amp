var _ = require('underscore');
var Backbone = require('backbone');


module.exports = Backbone.Model.extend({
  defaults: {
      title: 'Indicator Name',
      featurePath: 'http://example.com',
      type: 'raster', // maybe not necessary....
      admLevel: 0     // maybe not necessary....
  },

  initialize: function() {
    
  },

});
