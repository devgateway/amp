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
var ProjectSites = require('./collections/project-sites-collection');
var Title = require('./title');


var GISData = function(options) {
  this.initialize.apply(this, arguments);
};

_.extend(GISData.prototype, Backbone.Events, {

  initialize: function() {
    this.boundaries = new Boundaries();
    this.indicators = new Indicators([], { boundaries: this.boundaries });
    this.projectSites = new ProjectSites();

    this.title = new Title({ data: this });

    // bubble indicator events on the data object
    this.listenTo(this.indicators, 'all', this.bubbleIndicatorEvents);
  },

  load: function() {
    this.boundaries.fetch();
    this.indicators.fetch();
  },

  bubbleIndicatorEvents: function(eventName) {
    /*
     * Bubble some events, including namespaced versions of the event.
     *
     * If an indicator triggers 'show', data will bubble it as both 'show' and 'show:indicator'.
     * All arguments are forwarded.
     */
    if (_.contains('show hide loaded processed'.split(' '), eventName)) {
      this.trigger.apply(this, arguments);  // pass-through everything unmodified
      var namespacedName = eventName + ':indicator',
          args = _.tail(arguments);  // cut off the event name
      args.unshift(namespacedName);
      this.trigger.apply(this, args);
    }
  },

  getAllVisibleLayers: function() {
    var layers = _([]);

    // TODO: find a better way to merge these arrays
    this.indicators.getSelected().each(function(indicator) {
      layers.push(indicator);
    }, this);

    this.projectSites.getSelected().each(function(site) {
      layers.push(site);
    }, this);

    // TODO add clusters

    return layers.chain();
  }

});


module.exports = GISData;

