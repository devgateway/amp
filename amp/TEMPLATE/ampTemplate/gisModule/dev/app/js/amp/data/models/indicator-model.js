var _ = require('underscore');
var Backbone = require('backbone');

module.exports = Backbone.Model.extend({

  defaults: {
    title: undefined,
    type: undefined
  },

  select: function() {
    this.collection.select(this);
  },

  unselect: function() {
    this.collection.unselect(this);
  },

  toggle: function() {
//phil I'm not sure i get this array notation...maybe walk me through it and we'll 
// see if there is a more readable way with native backbone collection ops... findWhere etc.
    this.collection[this.get('selected') ? 'unselect' : 'select'](this);
  },

  getBoundary: function() {
    if (this.get('type') !== 'joinBoundaries') {
      throw new Error('non-"joinBoundaries" layers do not have boundaries.');
    }
    var boundaryLink = this.get('joinBoundariesLink');  // TODO: handle IDs vs links consitently
    var boundaryId = boundaryLink.split('gis/boundaries/')[1];
    return this.collection.boundaries.find(function(boundary) {
      return boundary.id === boundaryId;
    });
  }

});
