var _ = require('underscore');
var Backbone = require('backbone');
var JoinIndicator = require('../models/indicator-join-model');
var ArcGISDynamicLayerIndicator = require('../models/indicator-arcgis-dynamicLayer-model');
var WMSIndicator = require('../models/indicator-wms-model');
var LoadOnceMixin = require('../../mixins/load-once-mixin');

/* Backbone Collection IndicatorLayers (RENAME FILE) */
module.exports = Backbone.Collection
.extend(LoadOnceMixin).extend({

  url: '/rest/gis/indicator-layers',

  model: function(attrs) {
    var typeName = attrs.type;

    switch (typeName) {
      case 'joinBoundaries':
        return new JoinIndicator(attrs);
      case 'wms':
        return new WMSIndicator(attrs);
      case 'arcgis':
      case 'Indicator Layers':
        return new ArcGISDynamicLayerIndicator(attrs);
      default:
        console.error('Unrecognized indicator type. Check parse function.: ' + attrs.type);
        return new Backbone.Model();
    }
  },

  initialize: function(models, options) {
    this.boundaries = options.boundaries;
  },

  parse: function(data) {
    var parsedData = data;
    parsedData = _.filter(data, function(layer) {
      switch (layer.type) {
        case 'joinBoundaries':
        case 'wms':
        case 'arcgis':
        case 'Indicator Layers':
          return true;
        default:
          return false;
      }
    });

    return parsedData;
  },

  getSelected: function() {
    return this.chain()
      .filter(function(model) { return model.get('selected'); });
  }

});
