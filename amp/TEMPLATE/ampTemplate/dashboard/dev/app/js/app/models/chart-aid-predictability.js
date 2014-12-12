var _ = require('underscore');
var ChartModel = require('./chart-model-base');


module.exports = ChartModel.extend({

  _prepareTranslations: function() {
    var self = this;
    var predictabilityBaseLanguage = {};

    /* Prepare the translations for the chart */
    predictabilityBaseLanguage['amp.dashboard:predictability-planned'] = 'Planned';
    predictabilityBaseLanguage['amp.dashboard:predictability-actual'] = 'Actual';

    this.localizedPredictability = this.app.translator.translateList(predictabilityBaseLanguage).then(
      function(localizedPredictabilityList) {
        self.localizedPredictabilityList = localizedPredictabilityList;
      });
  },

  parse: function(data) {
    function pick(which) {
      return function(d) {
        return {
          x: d.year,
          y: d[which]
        };
      };
    }
    /* returns from map() like [{amp.gis:title-Region: 'Region'}, ... ]*/

    data.processed = [
      {
        key: this.localizedPredictabilityList['amp.dashboard:predictability-planned'],
        values: _(data.years).map(pick('planned'))
      },
      {
        key: this.localizedPredictabilityList['amp.dashboard:predictability-actual'],
        values: _(data.years).map(pick('actual'))
      }
    ];
    return data;
  }

});
