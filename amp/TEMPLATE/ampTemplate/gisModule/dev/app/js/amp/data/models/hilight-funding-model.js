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

      // important to trigger after fetch / load.
      this.trigger(show ? 'show' : 'hide', this);
    });


    this.listenTo(this.collection.filter, 'apply', this.refreshModel);
    this.listenTo(this.collection.settings, 'change:selected', this.refreshModel);
    this.listenTo(this, 'sync', this.refresh);
  },

  refresh: function() {
    this.loadBoundary();
    this.updatePaletteRange();
  },

  // if layer is selected update it.
  refreshModel: function() {
    if (this.get('selected')) {
      this.fetch();
    }
  },

  parse: function(data) {
    if (data.values && data.values.length > 0 && data.values[0].admID) {
      // hacky fix for adm0 of funding type. make sure in topojson admid of adm0 is 0
      if (data.values[0].admID === 'GeoId: Undefined') {
        data.values[0].admID = 0;
      }
    }
    return data;
  },

  fetch: function(options) {
    var payload = {otherFilters: {}};

    // get filters
    if (this.collection.filter) {
      _.extend(payload, this.collection.filter.serialize());
    }

    //get settings
    if (this.collection.settings && !_.isEmpty(this.collection.settings.serialize())) {
      payload.settings = this.collection.settings.serialize();
    }


    options = _.defaults((options || {}), {
      type: 'POST',
      data: JSON.stringify(payload)
    });

    return Backbone.Model.prototype.fetch.call(this, options);
  }
});
