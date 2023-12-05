/*/A
 * The GIS Data Engine
 * attaches to app.data
 */

var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');

var Filter = require('amp-filter/src/main');

var Title = require('./title');
var SavedMaps = require('./collections/saved-maps-collection');
var Activities = require('./collections/activity-collection');
var Boundaries = require('./collections/boundary-collection');
var Indicators = require('./collections/indicator-collection');
var StructuresMenu = require('./models/structures-menu-model'); /*a.k.a. structures */
var ADMClusters = require('./collections/adm-cluster-collection');
var HilightFundingCollection = require('./collections/hilight-funding-collection');
var Settings = require('amp-settings/src/index');


var Structures = require('./collections/structures-collection'); /*a.k.a. structures */
var IndicatorTypes = require('./collections/indicator-type-collection');
var User = require('./models/amp-user-model.js');

var GISData = function() {
  this.initialize.apply(this, arguments);
};


_.extend(GISData.prototype, Backbone.Events, {

  layerEvents: ['show', 'hide', 'refresh', 'loaded', 'processed', 'sync'],

  initialize: function() {  
	_.bindAll(this, 'initializeCollectionsAndModels');
    this.savedMaps = new SavedMaps([], {appData: this});
    this.title = new Title({ data: this });    
  },
  
  initializeCollectionsAndModels: function(){
	  var self = this;
	  /* stub filled in by Filters service */
	    this.filter = new Filter({
	      draggable: true,
	      caller: 'GIS'
	    });
	    // forces filter to start loading list immediately. TODO: move to an option for filter init.
	   this.filter.view._getFilterList();
	   this.boundaries = new Boundaries();
	   this.settingsWidget = new Settings.SettingsWidget({
	  		draggable : true,
	  		caller : 'GIS',
	  		isPopup: false,
	  		definitionUrl: '/rest/settings-definitions/gis'
	   });
	
	   this.generalSettings = new Settings.GeneralSettings();
	   this.generalSettings.load();
	   this.indicatorTypes = new IndicatorTypes();
	   this.user = new User();

	    //setup performance rule model
	    var PerformanceToggleModel = Backbone.Model.extend({defaults: {isPerformanceToggleAvailable: false, isPerformanceToggleSelected: null}});
	    this.performanceToggleModel = new PerformanceToggleModel();	    
	    $.ajax({
			  url: '/rest/gis/has-enabled-performance-rules'			 
	      }).done(function(data) {	    	  
	    	  self.performanceToggleModel.set('isPerformanceToggleAvailable', data);
		});		

	    this.activities = new Activities([], {
	      settingsWidget: this.settingsWidget,
	      filter: this.filter,
	      pageSize: 15,
	      appData: this
	    });

	    this.structures = new Structures([], {
	      settingsWidget: this.settingsWidget,
	      filter: this.filter,
	      appData: this
	    });

	    this.structuresMenu = new StructuresMenu([
	      {}  // just the one model, all defaults
	    ], {
	      settingsWidget: this.settingsWidget,
	      filter: this.filter,
	      appData: this
	    });


	    this.indicators = new Indicators([], { boundaries: this.boundaries, settingsWidget: this.settingsWidget, generalSettings: this.generalSettings, performanceToggleModel: this.performanceToggleModel});

	    this.admClusters = new ADMClusters([], {
	      boundaries: this.boundaries,
	      filter: this.filter,
	      settingsWidget: this.settingsWidget,
	      performanceToggleModel: this.performanceToggleModel
	    });

	    // TODO get these from the api
	    this.hilightFundingCollection = new HilightFundingCollection([],
	      { boundaries: this.boundaries, filter: this.filter, settingsWidget: this.settingsWidget });  
	 
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

    $.when(this.filter.loaded, this._stateWait, this.settingsWidget.definitions.loaded,this.generalSettings.loaded).then(function() {
      self.boundaries.load();
      self.indicatorTypes.load();
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

