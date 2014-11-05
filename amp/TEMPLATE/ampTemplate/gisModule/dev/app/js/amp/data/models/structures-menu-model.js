/*
 * This model is effectively a singleton, handling state for the project sites.
 */

var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');
var Palette = require('../../colours/colour-palette');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Structures = require('../collections/structures-collection');



var ONAMES = {  // TODO: temp hack until filters have a proper data structure
  3001153: 'Care Austria',
  2: 'African Development Fund (AFDF)',
  3: 'Asian Development Bank (ASDB)',
  4: 'Asian Development Fund (ASDF)',
  73: 'Joint United Nations Programme on HIV/AIDS (UNAIDS)',
  74: 'UNICEF',
  75: 'World Bank',
  3267: 'Ministère du Plan',
  3204:  'World Bank',
  3253: 'Global Partnership for Education',
  3351: 'CARE International UK',
  3856:  'Icelandic International Development Agency (ICEIDA)',
  4192: 'UNICEF',
  4211: 'Canadian International Development Agency (CIDA)',
  3570: 'African Development Fund (AFDF)',
  105045712: 'Deutsche Gesellschaft fur Internationale Zusammenarbeit (GIZ)',
  40279598: 'India EXIM Bank',
  24: 'International Fund for Agricultural Development (IFAD)',
  110592388: 'Canadian International Development Agency (CIDA)',
  85180698: 'CARE International Nepal',
  81831902: 'Centers for Disease Control and Prevention',
  57927714: 'Australian Agency for International Development (AUSAID)',
  57299431: 'Danish Church Aid',
  40001642: 'Global Partnership for Education',
  104929646: 'Danida - Danish International Development Agency',
  51973501: 'CARE International UK',
  110592567: 'Japan International Cooperation Agency (JICA)',
  54295162: 'International Labour Organisation (ILO)',
  64008061: 'International Organisation for Migration (IOM)',
  52178366: 'Icelandic International Development Agency (ICEIDA)'
};



module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({


  defaults: {
    title: 'Project Sites',
    value: '',
    helpText: ''
  },

  initialize: function(things, options) {
    var self = this;

    this.activities = options.activities;
    this.appData = options.appData;
    this.filter = options.filter;
    this.palette = new Palette.FromSet();
    this.projectSitesCollection = this.appData.projectSites;

    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
      if (show) {
        self.appData.projectSites.fetch();
      }
    });
    this.listenTo(this, 'sync', this.updatePaletteSet);
    this.listenTo(this.filter, 'apply', this.applyFilters);
  },

  applyFilters: function(serializedFitlers) {
    if (this.get('selected')) {
      this.appData.projectSites.fetch();
    }
  },


  //TODO: we don't want to do loadonce, because fitlers change...so fetch instead,
  load: function() {
    //return this.fetch();
    console.err("removeme");
    return self.appData.projectSites.fetch();
    //DRS temp try fetch for testing filtering..
  },

  // Loads structures and all their activitites.
  loadAll: function() {
    var self = this;
    var deferred = $.Deferred();
    var allActivityIds = [];

    self.appData.projectSites.fetch().then(function() {
      deferred.resolve();

    });

    return deferred;
  },

  // does not do new web requests, unless it's never been done.
  getSites: function() {
    if (_.has(this, '_loaded')) {
      return this._loaded;
    } else {
      return this.loadAll();
    }
  },

  parse: function(data) {
    // TODO: don't keep data.features around
    data.sites = new Structures(data.features);

    return data;
  },

  getSelected: function() {
    return _.chain(this.get('selected') ? [this] : []);
  },

  updatePaletteSet: function() {
    var deferred = $.Deferred();
    var self = this;

    /*
    //load the necessary activities.
    this.getSites().done(_.bind(function() {
      var activity;

      var orgSites = self.projectSitesCollection
        .chain()
        .groupsBy(function(site) {

          //if (!_.isEmpty(self.activities.get(site.get('properties').activity))) {

            //// doesn't handle multiple activities, which may be introduced in the future..
            //activity = self.activities.get(site.get('properties').activity[0]);

            //// TODO:  for now we want just organizations[1]  for donor.
            //// Choosing a vertical will need to be configurable from drop down..
            //if (!_.isEmpty(activity.get('matchesFilters').organizations['1'])) {
              //return activity.get('matchesFilters').organizations['1'];
            //} else {
              //console.warn('Palette generation: Activity is missing desired vertical');
              //return -1;
            //}
          //} else {
            //console.warn('Palette generation: Structure is missing an activity');
            //return -1;
          //}
        })
        .map(function(sites, orgId) {
          return {
            id: orgId,
            name: ONAMES[orgId], // TODO: use filters for lookup once we have app.data.filters
            sites: _(sites).map(function(site) { return site.get('id'); })
          };
        })
        .sortBy(function(item) {
          return item.sites.length;
        })
        .reverse()
        .value();

      this.palette.set('elements', orgSites);
      deferred.resolve();

    }, this));
   */
      deferred.resolve();

    return deferred;
  }

});
