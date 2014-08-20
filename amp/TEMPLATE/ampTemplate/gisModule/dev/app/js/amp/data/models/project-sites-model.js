var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var Backbone = require('backbone');
var Palette = require('../../colours/colour-palette');



module.exports = Backbone.Model.extend({

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
  },

  load: function() {
    var self = this;

    // skip the work if we're already loaded
    if (_.isNull(this._dataLoaded)) {
      this._dataLoaded = new Deferred();
      this.fetch({type: 'POST', data:'{}'}).then(function() {
        self.updatePaletteSet();  // in here so we don't re-do it later
        self._dataLoaded.resolve();
      });
    }

    this._dataLoaded.then(function() {
      self.trigger('loaded processed', self);
    });

    return this._dataLoaded.promise();
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
