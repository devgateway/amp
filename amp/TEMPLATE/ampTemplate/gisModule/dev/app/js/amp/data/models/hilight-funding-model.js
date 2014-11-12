var _ = require('underscore');
var Backbone = require('backbone');
var IndicatorJoinModel = require('./indicator-join-model');


module.exports = IndicatorJoinModel.extend({
  type: 'POST',

  url: function() {
    var settings = this.collection.settings.serialize();
    var fundingType = 'ac';

    // TODO: ask Diego or Nadia to use same format / terms as settings...
    if (settings && settings[0]) {
      if (settings[0] === 'Actual Commitments') {
        fundingType = 'ac';
      } else if (settings[0] === 'Actual Disbursements') {
        fundingType = 'ad';
      } else if (settings[0] === 'Actual Expenditures') {
        fundingType = 'ae';
      }
    }

    return '/rest/gis/locationstotals/' + this.id.replace('-', '') + '/' + fundingType;
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

  parse: function(data){
    if (data.values && data.values.length > 0 && data.values[0].admID) {
      if (data.values[0].admID === 'GeoId: Undefined') {
        data.values[0].admID = 0;
      }
    }
    return data;
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
