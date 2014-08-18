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
var ADMClusters = require('./collections/adm-cluster-collection');
var ProjectSitesAndClusters = require('./collections/clusters-and-project-sites');
var Title = require('./title');


var GISData = function(options) {
  this.initialize.apply(this, arguments);
};

_.extend(GISData.prototype, Backbone.Events, {

  layerEvents: ['show', 'hide', 'loaded', 'processed'],

  initialize: function() {
    this.boundaries = new Boundaries();
    this.indicators = new Indicators([], { boundaries: this.boundaries });
    this.projectSites = new ProjectSites();
    this.admClusters = new ADMClusters([
      // TODO get these from the api
      {
        title: 'Projects by Province',
        value: 'adm-1'
      },
      {
        title: 'Projects by District',
        value: 'adm-2'
      }
    ], { boundaries: this.boundaries });
    this.sitesAndClusters = new ProjectSitesAndClusters({
      sites: this.projectSites,
      clusters: this.admClusters
    });

    this.title = new Title({ data: this });

    // bubble indicator events on the data object
    this.listenTo(this.indicators, 'all', this.bubbleLayerEvents('indicator'));
    this.listenTo(this.projectSites, 'all', this.bubbleLayerEvents('project-site'));
    this.listenTo(this.admClusters, 'all', this.bubbleLayerEvents('adm-cluster'));
  },

  load: function() {
    this.boundaries.fetch();
    this.indicators.fetch();
    // no need to fetch project sites (it's special)
    this.admClusters.fetch({ remove: false });  // also special for now
  },

  bubbleLayerEvents: function(namespace) {
    /*
     * Bubble some events, including namespaced versions of the event.
     *
     * If an indicator triggers 'show', data will bubble it as both 'show' and 'show:indicator'.
     * All arguments are forwarded.
     */
    var namespacer = _.template('<%= ev %> <%= ev %>:' + namespace);

    return function(eventName) {
      if (_.contains(this.layerEvents, eventName)) {
        var args = _.tail(arguments);  // everything after eventName
        args.unshift(namespacer({ ev: eventName }));  // prepend the events to triger
        this.trigger.apply(this, args);
      }
    };
  },

  getAllVisibleLayers: function() {
    var layers = _.union(
      this.indicators.getSelected().value(),
      this.sitesAndClusters.getSelected().value()
    );

    return _.chain(layers);
  }

});


module.exports = GISData;

