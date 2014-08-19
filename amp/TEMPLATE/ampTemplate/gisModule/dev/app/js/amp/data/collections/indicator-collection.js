var Backbone = require('backbone');
var RadioMixin = require('../../mixins/radio-mixin');
var JoinIndicator = require('../models/indicator-join-model');
var ArcGISIndicator = require('../models/indicator-arcgis-model');
var WMSIndicator = require('../models/indicator-wms-model');


module.exports = Backbone.Collection
  .extend(RadioMixin)  // manages 'selected' state and api of models
  .extend({

  url: '/rest/gis/indicator-layers',

  model: function(attrs) {
    var typeName = attrs.type;
    if (typeName === 'joinBoundaries') {
      return new JoinIndicator(attrs);
    } else if (typeName === 'wms') {
      return new WMSIndicator(attrs);
    } else if (typeName === 'arcgis') {
      return new ArcGISIndicator(attrs);
    } else {
      throw new Error('Unrecognized indicator type: ' + attrs.type);
    }
  },

  initialize: function(models, options) {
    this.boundaries = options.boundaries;
  }

});
