var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;
  },

  parse: function(data) {
    function pick(which) {
      return function(d) {
        return {
          x: parseInt(d.year, 10),
          y: d[which]
        };
      };
    }
    var endOffset = -2,  // totals are at the end grr
        initialOffset = -(6 - endOffset);  // 5 + 1 to match other charts
    data.processed = [
      { key: 'Planned',
        values: _(data.years).map(pick('planned')).slice(initialOffset, endOffset)
      },
      {
        key: 'Actual',
        values: _(data.years).map(pick('actual')).slice(initialOffset, endOffset)
      }
    ];
    return data;
  }

});
