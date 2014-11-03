var when = require('jquery').when;
var _ = require('underscore');
var $ = require('jquery');
var Backbone = require('backbone');
var IndicatorJoinModel = require('./indicator-join-model');


module.exports = IndicatorJoinModel.extend({
  type: 'POST',
  url: function() {
    return '/rest/gis/locationstotals/' + this.id.replace('-', '') + '/ac';
  },

  initialize: function() {
    this.set('type', 'joinBoundaries');
    IndicatorJoinModel.prototype.initialize.call(this, arguments);


    this.listenTo(this, 'change:selected', function(blah, show) {
      // this is ugly, but necesesary to stop showLayers call to /load from triggering another fetch.
      if (show) {
        if (!this._loaded) {
          this.load();
        } else {
          this.fetch();
        }
      }
    });

    this.listenTo(this.collection.filter, 'apply', this.applyFilters);
    this.listenTo(this, 'sync', this.refresh);
  },

  refresh: function() {
    this.loadBoundary();
    this.updatePaletteRange();
  },

  // if filters change and layer is selected update it.
  applyFilters: function() {
    if (this.get('selected')) {
      this.fetch();
    }
  },

  fetch: function(options) {
    var filter = {otherFilters: {}};
    // get filters
    if (this.collection.filter) {
      _.extend(filter, this.collection.filter.serialize());
    }

    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(filter)
    });

    return Backbone.Model.prototype.fetch.call(this, options);
  }
});
