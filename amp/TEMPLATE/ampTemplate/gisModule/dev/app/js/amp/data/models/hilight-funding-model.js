var Backbone = require('backbone');
var _ = require('underscore');
var $ = require('jquery');
var IndicatorJoinModel = require('./indicator-join-model');


module.exports = IndicatorJoinModel.extend({
  type: 'POST',

  url: function() {
    return '/rest/gis/locationstotals/' + this.id;
  },

  initialize: function() {
    this.set('type', 'joinBoundaries');
    IndicatorJoinModel.prototype.initialize.call(this, arguments);

    // if filters or settings change then get data from API and update model
    this.listenTo(this.collection.filter, 'apply', this.refreshModel);
    this.listenTo(this.collection.settingsWidget, 'applySettings', this.refreshModel);

    // If the model updates, then refresh dependencies.
    this.listenTo(this, 'sync', this.refreshDependencies);
  },

  refreshDependencies: function() {
    this.loadBoundary();
    this.updatePaletteRange();
  },

  // if layer gets selected update it.
  refreshModel: function() {
    // this forces next 'load' call to do a fresh fetch.
    delete this._loaded;

    if (this.get('selected')) {
      this.load();
    }
  },

  parse: function(data) {
    if (data.values && data.values.length > 0 && data.values[0].admID) {
      // hacky fix for adm0 of funding type. make sure in topojson admid of adm0 is 0
      if (data.values[0].admID === 'GeoId: Undefined') {
        data.values[0].admID = 0;
      }
    }

    data.unit = data.currency;

    // Removes all entries where geoId is null
    data.values = _.filter(data.values, function(value) {
      return value.admID;
    });

    // convert 'amount' to 'value' in API so parse not needed. and can be conssistant in custom joins
    _.each(data.values, function(value) {
      value.value = value.amount;
      value.geoId = value.admID;
    });


    return data;
  },

  fetch: function(options) {
    var self = this;
    var payload = {};
    var deferred = $.Deferred();

    // get filters
    if (this.collection.filter) {
      _.extend(payload, this.collection.filter.serialize());
    }

    //get settings
    if (this.collection.settingsWidget) {
           //add settings to payload
        payload.settings = this.collection.settingsWidget.toAPIFormat();
        options = _.defaults((options || {}), {
          type: 'POST',
          data: JSON.stringify(payload)
        });

        // call normal fetch now
        Backbone.Model.prototype.fetch.call(self, options).then(function() {
          deferred.resolve();
        });
    } else {
      console.warn('no settings fail hilight funding!');
      deferred.resolve(null);
    }

    return deferred;
  }
});