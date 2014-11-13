var _ = require('underscore');
var $ = require('jquery');
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

  loadAll: function() {
    var self = this;
    var deferred = $.Deferred();

    this.load().then(function() {
      self.url = '/rest/gis/indicators';
      self.fetch({remove: false}).then(function() {
        deferred.resolve();
      });
    });

    return deferred;
  },

  parse: function(data) {
    var self = this;
    var parsedData = data;
    parsedData = _.filter(data, function(layer) {
      switch (layer.type) {
        case 'joinBoundaries':
        case 'wms':
        case 'arcgis':
        case 'Indicator Layers':
          return true;
        default:
      }

      // this is a custom one. API is a bit messy so we do fair bit of manual work.
      if (layer.colorRamp) {
        layer.title = layer.name;
        layer.type = 'joinBoundaries';
        layer.adminLevel = self._magicConversion(layer.admLevelId);
        layer.unit = 'unit';
        return true;
      }

      return false;
    });

    return parsedData;
  },

  getSelected: function() {
    return this.chain()
      .filter(function(model) { return model.get('selected'); });
  },

  _magicConversion: function(textAdm) {
    var magicWords = {
      Country: 'adm-0',
      Region: 'adm-1',
      Zone: 'adm-2',
      District: 'adm-3'
    };

    return magicWords[textAdm];
  }

});
