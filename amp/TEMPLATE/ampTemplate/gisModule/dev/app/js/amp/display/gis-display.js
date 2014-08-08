/*
 * The GIS Display Engine
 *
 * This module is a bridge between the pure data layer (see ../data/gis-data),
 * and the components which ultimately display that data. Its scope is to
 * manage display-related state.
 *
 * Some examples:
 *  -> Exposing a dynamic generated title for tha map
 *  -> Managing colours used in the display of the data (and legend, etc)
 *
 * The idea is to make the components that display stuff dumb, and centralize
 * all that logic and state here, in hopes that creating new components (which
 * may want access to any kind of existing display state) can be created
 * easily, without having to touch much of the rest of the system.
 */

var _ = require('underscore');
var Backbone = require('backbone');
var Indicators = require('./collections/indicator-collection');
var Title = require('./title');


var GISDisplay = function(options) {
  this.data = options.data;
  this.initialize.apply(this, arguments);
};

_.extend(GISDisplay.prototype, Backbone.Events, {

  initialize: function() {
    var initArgs = {display: this, data: this.data};
    this.title = new Title(initArgs);
    this.indicators = new Indicators([], initArgs);

    // TODO david -- this should listen for any newly added layer type
    // ... or maybe this event should be triggered in data?
    this.listenTo(this.data.indicators, 'change:selected', this.layersUpdated);
  },

  layersUpdated: function() {
    this.trigger('update:layers');
  }

});


module.exports = GISDisplay;
