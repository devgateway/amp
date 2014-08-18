var _ = require('underscore');
var Backbone = require('backbone');
var RadioMixin = require('../../mixins/radio-mixin');


module.exports = Backbone.Collection
  .extend(RadioMixin)
  .extend({

  _watched: _([]),

  initialize: function(options) {
    this.watchCollection(options.sites);
    this.watchCollection(options.clusters);
    this.stuffUpdated();
  },

  watchCollection: function(collection) {
    this._watched.push(collection);
    // hijack an expected method on the collections for RadioMixin
    collection.toggleSelect = _.bind(this.toggleSelect, this);
    this.listenTo(collection, 'all', this.trigger);
    this.listenTo(collection, 'all', this.stuffUpdated);
  },

  stuffUpdated: function(ev) {
    this.reset(_.union.apply(this, this._watched.map(function(collection) {
      return collection.models;
    })));
  }

});
