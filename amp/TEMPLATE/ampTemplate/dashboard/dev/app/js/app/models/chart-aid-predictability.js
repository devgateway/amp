var _ = require('underscore');
var ChartModel = require('./chart-model-base');


module.exports = ChartModel.extend({
	

	defaults : {
		title : '',
		showPlannedDisbursements: true,
		showActualDisbursements: true,
		chartType: 'aidPredictability'
	},

  _prepareTranslations: function() {
    var self = this;
    var predictabilityBaseLanguage = {};

    /* Prepare the translations for the chart */
    predictabilityBaseLanguage['amp.dashboard:aid-predictability-planned-disbursements'] = 'Planned Disbursements';
    predictabilityBaseLanguage['amp.dashboard:aid-predictability-actual-disbursements'] = 'Actual Disbursements';

    this.localizedPredictability = this.app.translator.translateList(predictabilityBaseLanguage).then(
      function(localizedPredictabilityList) {
        self.localizedPredictabilityList = localizedPredictabilityList;
      });
  },

  parse: function(data) {
	this.set('title', data.title);	
	if(!_.isUndefined(data.showPlannedDisbursements)){
		this.set('showPlannedDisbursements', data.showPlannedDisbursements);
	}
	if(!_.isUndefined(data.showActualDisbursements)){
		this.set('showActualDisbursements', data.showActualDisbursements);
	}	
	
    function pick(which) {
      return function(d) {
        return {
          x: d.year,
          y: d[which]['amount'],
          z: d[which]['formattedAmount']
        };
      };
    }
    /* returns from map() like [{amp.gis:title-Region: 'Region'}, ... ]*/

    data.processed = [
      {
        key: this.localizedPredictabilityList['amp.dashboard:aid-predictability-planned-disbursements'],
        originalKey: 'planned',
        values: _(data.years).map(pick('planned disbursements')),
        disabled: !this.get('showPlannedDisbursements')
      },
      {
        key: this.localizedPredictabilityList['amp.dashboard:aid-predictability-actual-disbursements'],
        originalKey: 'actual',
        values: _(data.years).map(pick('actual disbursements')),
        disabled: !this.get('showActualDisbursements')
      }
    ];
    return data;
  }

});
