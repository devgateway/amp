/*
 * This model is effectively a singleton, handling state for the project sites.
 */

var _ = require('underscore');
var Backbone = require('backbone');
var $ = require('jquery');
var Palette = require('../../colours/colour-palette');
var LoadOnceMixin = require('../../mixins/load-once-mixin');

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

    this.listenTo(this, 'change:selected', function(other, show) {
      this.trigger(show ? 'show' : 'hide', this);
      console.log('changes:', arguments);
      if (show) {
        self.projectSitesCollection.fetch();
      }
    });

    this.listenTo(this.projectSitesCollection, 'sync', this.updatePaletteSet);
    this.listenTo(this.filter, 'apply', this.applyFilters);
  },

  applyFilters: function() {
    if (this.get('selected')) {
      this.projectSitesCollection.fetch();
    }
  },


  load: function() {
    return this.projectSitesCollection.load();
  },

  // Loads structures and all their activities.
  loadAll: function() {
    return this.projectSitesCollection.loadAll();
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

  updatePaletteSet: function() {
    var deferred = $.Deferred();

    /*
    var self = this;
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
