var _ = require('underscore');
var Backbone = require('backbone');
var ProjectLayerModel = require('../models/project-layer-model');

var APIHelper = require('../../../../libs/local/api-helper');

module.exports = Backbone.Collection.extend({
  model: ProjectLayerModel

});
