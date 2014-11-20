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
    filterVertical: 'Primary Sector Id'
  },

  initialize: function(things, options) {
    var self = this;
    this.appData = options.appData;
    this.filter = options.filter;
    this.structuresCollection = this.appData.projectSites;

    this.listenTo(this, 'change:filterVertical', function() {
      self.structuresCollection.updatePaletteSet();
    });
  },

  attachListeners: function() {
    var self = this;

    this.listenTo(this, 'change:selected', function(other, show) {
      this.trigger(show ? 'show' : 'hide', this);
      if (!self.structuresCollection._lastFetch) {
        self.structuresCollection.fetchStructuresWithActivities();
      }
    });

    this.listenTo(this.filter, 'apply', this.applyFilters);
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
  }

});
