var IndicatorJoinModel = require('./indicator-join-model');


module.exports = IndicatorJoinModel.extend({

  url: function() {
    return '/rest/gis/locationstotals/' + this.id.replace('-', '') + '/ac';
  },

  initialize: function() {
    this.set('type', 'joinBoundaries');
    IndicatorJoinModel.prototype.initialize.call(this, arguments);
  }
});
