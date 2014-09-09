/*
 * This model is effectively a singleton, handling state for the project sites.
 */

var _ = require('underscore');
var Backbone = require('backbone');
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
  .extend(LoadOnceMixin)
  .extend({

  url: '/rest/gis/structures',

  defaults: {
    title: 'Project Sites',
    value: '',
    helpText: ''
  },

  initialize: function(things, options) {
    this.activities = options.activities;
    this.palette = new Palette.FromSet();

    this.listenTo(this, 'change:selected', function(blah, show) {
      this.trigger(show ? 'show' : 'hide', this);
    });
    this.listenTo(this, 'sync', this.updatePaletteSet);
  },

  fetch: function(options) {
    options = _.defaults((options || {}), {
      type: 'POST',
      data:'{}'
    });
    return Backbone.Model.prototype.fetch.call(this, options);
  },

  // David replacec this with: _loadActivities
  load: function() {
    //this.activities.load();  // not needed right away, but start it as early as possible
    
    return LoadOnceMixin.load.apply(this).then(this._loadActivities);
  },

  parse: function(data) {
    // TODO: don't keep data.features around
    data.sites = new Structures(data.features);

    return data;
  },

  // Loads all activities for these structures/sites.
  _loadActivities: function(){
    var allActivityIds = [];
    // join all activity ids
    this.get('sites').each(function(site){
      allActivityIds = _(allActivityIds).union(allActivityIds,site.get('properties').activity);
    });

    return this.activities.getActivites(allActivityIds);
  },

  updatePaletteSet: function() {
    var self = this;

    //load the necessary activities.
    this._loadActivities().done(_.bind(function(activities) {
      var activity;

      var orgSites = this.get('sites')
        .chain()
        .groupsBy(function(site) {
          // TODO: only grabs first activity...does't handle multiple activities, 
          // which may be introduced in the future..
          activity = self.activities.get(site.get('properties').activity[0]); 
          //TODO: / Phil: organizations is a map of arrays, don't think this code does what we want...
          // I think we want just organizations[1]  for donor.
          // I asked Julian about API, returning funny values for org role..
          return activity.get('matchesFilters').organizations; 
        })
        .map(function(sites, orgId) {
          return {
            id: orgId,
            name: ONAMES[orgId],
            sites: _(sites).map(function(site) { return site.get('id'); })
          };
        })
        .sortBy(function(item) {
          return item.sites.length;
        })
        .reverse()
        .value();
      this.palette.set('elements', orgSites);

    }, this));
  }

});
