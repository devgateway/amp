var _ = require('underscore');
var Backbone = require('backbone');
var Palette = require('../collections/colour-palette');


module.exports = Backbone.Model.extend({

  defaults: {
    data: undefined,
    title: undefined,
    palette: undefined
  },

  initialize: function(options) {
    this.display = options.display;
    this.data = options.data;
    this.indicator = options.indicator;
    this.set('title', options.indicator.get('title'));
    this.palette = new Palette.FromRange({ seed: this.indicator.id});
    this.listenTo(this.indicator, 'change:selected', this.triggerShowHide);

    if (_.has(this.indicator.attributes, 'geoJSON')) {
      this.listenTo(this.indicator, 'change:geoJSON', this.updateGeoJSON);
    }
  },

  triggerShowHide: function(indicator, show) {
    if (show) {
      this.trigger('show', this);
    } else {
      this.trigger('hide', this);
    }
  },

  updateGeoJSON: function() {
    var min = +Infinity,
        max = -Infinity;
    _.each(this.indicator.get('data').values, function(value) {
      if (value.value < min) {
        min = value.value;
      }
      if (value.value > max) {
        max = value.value;
      }
    });
    this.palette.set({min: min, max: max});
  }

});
