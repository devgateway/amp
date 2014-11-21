var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;

    var self = this;
    var predictabilityBaseLanguage = {};

    /* Prepare the translations for the chart */
    predictabilityBaseLanguage['amp.dashboard:predictability-planned'] = 'Planned';
    predictabilityBaseLanguage['amp.dashboard:predictability-actual'] = 'Actual';
    predictabilityBaseLanguage = predictabilityBaseLanguage;
    this.localizedPredictability = this.app.translator.translateList(predictabilityBaseLanguage).then(
      function(localizedPredictabilityList) {
        self.localizedPredictabilityList = localizedPredictabilityList;
    });

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
    /* returns from map() like [{amp.gis:title-Region: 'Region'}, ... ]*/

      data.processed = [
        {
          key: this.localizedPredictabilityList['amp.dashboard:predictability-planned'],
          values: _(data.years).map(pick('planned')).slice(initialOffset, endOffset)
        },
        {
          key: this.localizedPredictabilityList['amp.dashboard:predictability-actual'],
          values: _(data.years).map(pick('actual')).slice(initialOffset, endOffset)
        }
      ];
    return data;
  }

});
