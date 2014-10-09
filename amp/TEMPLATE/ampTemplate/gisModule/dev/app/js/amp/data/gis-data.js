/*
 * The GIS Data Engine
 * attaches to app.data
 */

var _ = require('underscore');
var Backbone = require('backbone');

var Title = require('./title');
var SavedMaps = require('./collections/saved-maps-collection');
var Activities = require('./collections/activity-collection');
var Boundaries = require('./collections/boundary-collection');
var Indicators = require('./collections/indicator-collection');
var ProjectSites = require('./models/structures-collection-model'); /*a.k.a. structures */
var ADMClusters = require('./collections/adm-cluster-collection');
var Settings = require('./collections/settings-collection');

var ProjectSitesAlt = require('./collections/structures-collection'); /*a.k.a. structures */

var GISData = function() {
  this.initialize.apply(this, arguments);
};


_.extend(GISData.prototype, Backbone.Events, {

  layerEvents: ['show', 'hide', 'loaded', 'processed'],

  initialize: function() {
    this.savedMaps = new SavedMaps();

    this.activities = new Activities();
    this.boundaries = new Boundaries();
    this.settings = new Settings();
    this.projectSites = new ProjectSites([
      {}  // just the one model, all defaults
    ], { activities: this.activities});

    this.projectAlt = new ProjectSitesAlt();

    /* stub filled in by Filters service */
    this.filters = null;

    this.indicators = new Indicators([], { boundaries: this.boundaries });


    this.admClusters = new ADMClusters([
      // TODO get these from the api
      {
        title: 'Projects Country-wide',
        value: 'adm-0'
      },
      {
        title: 'Projects by Province',
        value: 'adm-1'
      },
      {
        title: 'Projects by District',
        value: 'adm-2'
      }
    ], { boundaries: this.boundaries });

    this.title = new Title({ data: this });

    // bubble indicator events on the data object
    this.listenTo(this.indicators, 'all', this.bubbleLayerEvents('indicator'));
    this.listenTo(this.projectSites, 'all', this.bubbleLayerEvents('structure'));
    this.listenTo(this.admClusters, 'all', this.bubbleLayerEvents('adm-cluster'));
  },

  load: function() {
    // this.activities.fetch();
    this.boundaries.fetch();
    this.indicators.fetch();
    // no need to fetch structures (they're special)
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
      this.projectSites.getSelected().value(),
      this.admClusters.getSelected().value()
    );

    return _.chain(layers);
  }

});


module.exports = GISData;

