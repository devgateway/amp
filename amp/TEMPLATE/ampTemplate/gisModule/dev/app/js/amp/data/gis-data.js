/*
 * The GIS Data Engine
 *
 * This module in intended to be:
 *   * Commanded: instructed to change state in a specific way
 *   * Requested: queried about properties and possibilities
 *   * Observed: its main job is to control and expose the data-level state
 *
 * It should probably be instantiated exactly once when running an application.
 *
 */

var _ = require('underscore');
var Backbone = require('backbone');
var Boundaries = require('./collections/boundary-collection');
var Indicators = require('./collections/indicator-collection');


var GISData = function(options) {
  this.initialize.apply(this, arguments);
};

_.extend(GISData.prototype, Backbone.Events, {

  initialize: function() {
    this.boundaries = new Boundaries();
    this.indicators = new Indicators({ boundaries: this.boundaries });
  },

  load: function() {
    this.boundaries.fetch();
    this.indicators.fetch();
  },

  // Phil, I think we should be a method to indicator-collection, we should discuss,
  // but I think less logic in this obj the better...unless this is for getting all types of layers...?
  getSelectedLayers: function() {
    var layers = _([]);

    this.indicators.getSelected().each(function(indicator) {
      layers.push(indicator);
    }, this);

    return layers.chain();
  }

});


module.exports = GISData;

