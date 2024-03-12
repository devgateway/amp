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
    0: '0',  // used for 'various'
    1: '1',  // None
    100: '100',
    110: '110',
    111: '111',
    112: '112',
    113: '113',
    114: '114',
    120: '120',
    121: '121',
    122: '122',
    130: '130',
    140: '140',
    150: '150',
    160: '160',
    200: '200',
    210: '210',
    220: '220',
    230: '230',
    240: '240',
    250: '250',
    300: '300',
    311: '311',
    312: '312',
    313: '313',
    321: '321',
    322: '322',
    323: '323',
    331: '331',
    332: '332',
    400: '400',
    500: '500',
    600: '600',
    700: '700',
    910: '910',
    920: '920',
    930: '930',
    410: '410',
    520: '520',
    530: '530',
    720: '720',
    730: '730',
    740: '740',
    998: '998'
  },
  
  DEFAULT_ICON_CODE: '998', //if no icon can be found using the sector code in the activity, default to unspecified

  getSelectedIconStyleCode: function(sectorCode) {
      var iconStyleCode = this.DEFAULT_ICON_CODE;
      if (this.iconMappings[sectorCode]) {
          iconStyleCode = sectorCode;
      }

      return 'svg-map-icon-' + iconStyleCode;
  }

});
