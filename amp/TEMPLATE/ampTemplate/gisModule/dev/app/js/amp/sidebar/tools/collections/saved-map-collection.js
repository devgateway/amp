var Backbone = require('backbone');
var SavedMapModel = require('../models/saved-map-model');


module.exports = Backbone.Collection.extend({

  model: SavedMapModel,

  url: '/rest/gis/states'

});


