/*/A
 * The GIS Data Engine
 * attaches to app.data
 */

var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');

var Filter = require('amp-filter/src/main');
var translator = require('../services/translator');

var Title = require('./title');
var SavedMaps = require('./collections/saved-maps-collection');
var Activities = require('./collections/activity-collection');
var Boundaries = require('./collections/boundary-collection');
var Indicators = require('./collections/indicator-collection');
var StructuresMenu = require('./models/structures-menu-model'); /*a.k.a. structures */
var ADMClusters = require('./collections/adm-cluster-collection');
var HilightFundingCollection = require('./collections/hilight-funding-collection');
var Settings = require('./collections/settings-collection');

var Structures = require('./collections/structures-collection'); /*a.k.a. structures */

var GISData = function() {
  this.initialize.apply(this, arguments);
};


_.extend(GISData.prototype, Backbone.Events, {

  layerEvents: ['show', 'hide', 'refresh', 'loaded', 'processed', 'sync'],

  initialize: function() {
    this.translator = translator;

    this.savedMaps = new SavedMaps([], {appData: this});


    /* stub filled in by Filters service */
    this.filter = new Filter({
      draggable: true,
      caller: 'GIS'
    });
    // forces filter to start loading list immediately. TODO: move to an option for filter init.
    this.filter.view._getFilterList();


    this.boundaries = new Boundaries();
    this.settings = new Settings();
    this.activities = new Activities([], {
      settings: this.settings,
      filter: this.filter,
      pageSize: 15,
      appData: this
    });

    this.structures = new Structures([], {
      settings: this.settings,
      filter: this.filter,
      appData: this
    });

    this.structuresMenu = new StructuresMenu([
      {}  // just the one model, all defaults
    ], {
      filter: this.filter,
      appData: this
    });


    this.indicators = new Indicators([], { boundaries: this.boundaries });

    this.admClusters = new ADMClusters([], {
      boundaries: this.boundaries,
      filter: this.filter,
      settings: this.settings
    });

    // TODO get these from the api
    this.hilightFundingCollection = new HilightFundingCollection([],
      { boundaries: this.boundaries, filter: this.filter, settings: this.settings });

    this.title = new Title({ data: this });

    // bubble indicator events on the data object
    this.listenTo(this.indicators, 'all', this.bubbleLayerEvents('indicator'));
    this.listenTo(this.hilightFundingCollection, 'all', this.bubbleLayerEvents('highlightFunding'));
    this.listenTo(this.structuresMenu, 'all', this.bubbleLayerEvents('structure'));
    this.listenTo(this.structures, 'all', this.bubbleLayerEvents('structure'));
    this.listenTo(this.admClusters, 'all', this.bubbleLayerEvents('adm-cluster'));
  },

  addState: function(state) {
    this.state = state;
    //TODO emit event instead of manually calling children
    this.structuresMenu.addedState();
  },

  load: function() {
    var self = this;

    this._stateWait = new $.Deferred();
    if (this.savedMaps.length) {
      // a bit sketch....
      this.state.loadPromise.always(this._stateWait.resolve);
    } else {
      this._stateWait.resolve();
    }

    $.when(this.filter.loaded, this._stateWait).then(function() {
      self.boundaries.load();
      self.indicators.loadAll();

      //drs attach indicotr listnerneros here

      self.structuresMenu.attachListeners();

      self.admClusters.load();
      self.admClusters.attachListeners();
      self.hilightFundingCollection.load();

      // explicitly call update title because we may have missed encessary events on startup.
      self.title.updateTitle();
    });
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

  /* Go into every menu-linked model to return the selected items in each */
  getAllVisibleLayers: function() {
    var layers = _.union(
      this.indicators.getSelected().value(),
      this.hilightFundingCollection.getSelected().value(),
      this.structuresMenu.getSelected().value(),
      this.admClusters.getSelected().value()
    );

    return _.chain(layers);
  }

});


module.exports = GISData;

