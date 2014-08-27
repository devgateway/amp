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
      console.error('Unrecognized indicator type: ' + attrs.type);
      return new Backbone.Model(); 
      //TODO: phil I couldn't figure out what to return to tell the collection to
      // not create this model...
      // I thought best to not crash all indicators if typo/unsupported type. 
      // Hopefully we can have a convo soon on the pros and cons to blocking errors / silent errors.
      // I don't want to block working functionality due to isolated non-working functionality.
    }
  },

  initialize: function(models, options) {
    this.boundaries = options.boundaries;
  }

});
