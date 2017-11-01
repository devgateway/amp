/*
 * This model is effectively a singleton, handling state for the project sites.
 */

var _ = require('underscore');
var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');

module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({


  defaults: {
    title: 'Project Sites',
    value: '',
    helpText: '',
    filterVertical: 'Primary Sector'
  },

  initialize: function(things, options) {
    this.appData = options.appData;
    this.filter = options.filter;
    this.settingsWidget = options.settingsWidget;
    this.structuresCollection = this.appData.structures;
    this.attachListeners();
  },

  addedState: function() {
    var self = this;

    this.appData.state.register(this, 'structuresMenu', {
      get: function() { return {filterVertical: self.get('filterVertical')}; },
      set: function(stateBlob) {
        if (stateBlob) {
          self.set('filterVertical', stateBlob.filterVertical, {silent:true});
        }
      },
      empty: null
    });
  },

  attachListeners: function() {
    var self = this;

    this.listenTo(this, 'change:selected', function(other, show) {
      if (self.structuresCollection._lastFetch) {  // what does this do?
        this.trigger(show ? 'show' : 'hide', this);
      } else {
        self.structuresCollection.fetchStructuresWithActivities();
      }
    });

    this.listenTo(this.filter, 'apply', this.applyFilters);
    this.listenTo(this.settingsWidget, 'applySettings', this.applyFilters);

    this.listenTo(this, 'change:filterVertical', function() {
      self.structuresCollection.updatePaletteSet();
    });
    
    
    this.listenTo(this.appData.performanceToggleModel, 'change:isPerformanceToggleSelected', this.applyFilters);
    
  },

  applyFilters: function() {
    if (this.get('selected')) {
       this.structuresCollection.fetchStructuresWithActivities();
    }
  },


  load: function() {
    return this.structuresCollection.load();
  },

  // Loads structures and all their activities.
  loadAll: function() {
    return this.structuresCollection.loadAll();
  },

  // does not do new web requests, unless it's never been done.
  getSites: function() {
    if (_.has(this, '_loaded')) {
      return this._loaded;
    } else {
      return this.loadAll();
    }
  },

  getSelected: function() {
    return _.chain(this.get('selected') ? [this] : []);
  },

  iconMappings: {
	0: 'Multiple2.svg',  // used for 'various'
	1: 'None.svg',
    100: 'Social.svg',
    110: 'Education.svg',
    111: 'Education.svg',
    112: 'Education.svg',
    113: 'Education.svg',
    114: 'Education.svg',
    120: 'Health.svg',
    121: 'Health.svg',
    122: 'Health.svg',
    130: 'Population.svg',
    140: 'Water.svg',
    150: 'Gov.svg',
    160: 'OtherSocial.svg',
    210: 'Transport.svg',
    220: 'Communication.svg',
    230: 'Energy.svg',
    240: 'Banking.svg',
    250: 'Business.svg',
    311: 'Agriculture.svg',
    312: 'Forestry.svg',
    313: 'Fishing.svg',
    321: 'Industry.svg',
    322: 'MineralResources.svg',
    323: 'Construction.svg',
    331: 'Trade.svg',
    332: 'Tourism.svg',
    400: 'Multisector.svg',
    500: 'GeneralSupport.svg',
    600: 'Debt.svg',
    700: 'Humanitarian.svg',
    910: 'CostDonor.svg',
    920: 'NGO_Support.svg',
    930: 'Refugees.svg',
    410: 'Environmental_Protection.svg',
    520: 'Food_Security.svg',
    530: 'Other_Commodity.svg',
    720: 'Emergency_Response.svg',
    730: 'Reconstruction.svg',
    740: 'Disaster_Prevention.svg',
    998: 'Unspecified.svg'
  },
  
  DEFAULT_ICON_CODE: '998' //if no icon can be found using the sector code in the activity, default to unspecified

});
