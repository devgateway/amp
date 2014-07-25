var _ = require('underscore');
var Backbone = require('backbone');
var APIHelper = require('../../../../libs/local/api-helper');

var SavedMapModel = require('../models/saved-map-model');


module.exports = Backbone.Collection.extend({

  model: SavedMapModel,

  url: APIHelper.getAPIBase() + '/rest/gis/states',

});


