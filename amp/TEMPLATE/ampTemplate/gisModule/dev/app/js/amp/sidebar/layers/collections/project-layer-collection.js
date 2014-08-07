var _ = require('underscore');
var Backbone = require('backbone');
var ProjectLayerModel = require('../models/project-layer-model');
var RadioMixin = require('../../../mixins/radio-mixin');

var APIHelper = require('../../../../libs/local/api-helper');

var mixins = _.reduce([
  RadioMixin
], _.extend, {});



//TODO move projectLayerCollection to app.data
module.exports = Backbone.Collection.extend(mixins).extend({
  model: ProjectLayerModel,

  initialize : function(videos) {
    this.listenTo(this, 'change:selected', this._selectionChanged);
  },

  _selectionChanged: function(model, newSelectState){

    // child is selected, disable rest, and emit
    // TODO: move to app.data and let layer listen to its changes
    if(newSelectState){
      Backbone.trigger('MAP_LOAD_PROJECT_LAYER', model.get('value'));
    } else{ // no children selected
      Backbone.trigger('MAP_LOAD_PROJECT_LAYER', null);
    }
  }
});
