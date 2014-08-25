/*
 * This model is effectively a singleton, handling state for the project sites.
 */

var _ = require('underscore');
var Backbone = require('backbone');
var Palette = require('../../colours/colour-palette');
var LoadOnceMixin = require('../../mixins/load-once-mixin');
var Structures = require('../collections/project-sites-structures');



module.exports = Backbone.Model
  .extend(LoadOnceMixin)
  .extend({

  url: '/rest/gis/structures',

  defaults: {
    title: 'Project Sites',
    value: '',
    helpText: ''
  },

  initialize: function() {
    this.palette = new Palette.FromSet();

    // private load state tracking
    this._dataLoaded = null;

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

  load: function() {
    return LoadOnceMixin.load.apply(this)
      .done(function() {
        this.trigger('loaded processed', this);  // LEGACY
      });
  },

  parse: function(data) {
    // TODO: don't keep data.features around
    data.structures = new Structures(data.features);
    return data;
  },

  updatePaletteSet: function() {
    this.palette.set('elements', [
      'first thing',
      'second thing',
      'third thing',
      'fourth thing',
      'fifth thing',
      'sixth...',
      'seventh...',
      '...'
    ]);
  }

});
