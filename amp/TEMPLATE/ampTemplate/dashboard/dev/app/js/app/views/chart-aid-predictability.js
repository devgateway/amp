var d3 = require('d3');
var ChartViewBase = require('./chart-view-base');
var _ = require('underscore');


module.exports = ChartViewBase.extend({

  uiDefaults: {
    big: false,
    view: 'multibar',
    showTotal: true,
    showMeasuresSelector: true,
    showTopLegends: true,
    showCommonChartArea: true
  },
  events: function(){
      return _.extend({},ChartViewBase.prototype.events,{
          'click .nv-series' : 'changeChartColumns'
      });
  },  
  changeChartColumns: function(e){
	  var key = $(e.currentTarget).find('.nv-legend-text').text();
	  var plannedDisbursementTrn = app.translator.translateSync("amp.dashboard:aid-predictability-planned-disbursements","Planned Disbursements");
	  var actualDisbursementTrn = app.translator.translateSync("amp.dashboard:aid-predictability-actual-disbursements","Actual Disbursements");
	  if(key == plannedDisbursementTrn){
		  this.model.set('showPlannedDisbursements', !this.model.get('showPlannedDisbursements'));	
	  }else if(key == actualDisbursementTrn){
		  this.model.set('showActualDisbursements', !this.model.get('showActualDisbursements'));	
	  }	
  },
  chartViews: [
    'multibar',
    'table'
  ],

  chartOptions: {
    nvControls: false
  },

  downloadChartOptions: {
    nvControls: false,
    trimLabels: false
  },

  getTTContent: function(context) {
    var app = this.app;
    var of = app.translator.translateSync('amp.dashboard:of','of');
    var total = app.translator.translateSync('amp.dashboard:total','total');
    var units = app.translator.translateSync(app.settings.numberMultiplierDescription);

    // IMPORTANT: We assume this chart will ALWAYS show 2 data series.
    /* modify to be like chart-funding-type if adding more series */
    var index = -1;
    if (context.data[0].disabled === true) {
      index = 1;
    } else {
      index = context.series.index;
    }

    var header = context.x.raw + ' ' +
          app.translator.translateSync('amp.dashboard:aid-predictability-' +
          context.data[index].originalKey + '-' + this.model.get('measure'), '');

    var otherSeries = context.data[1 - index];  // WARNING: assumes only 2 series
    var otherHere = otherSeries.values[context.x.index];
    var line2Amount = 0;
    if (otherHere.y > 0) {
      line2Amount = context.y.raw / otherHere.y;
    }
    var line2 = '<b>' + d3.format('%')(line2Amount) +
        '</b>&nbsp<span>' + of + '</span>&nbsp' + context.x.raw +
        '&nbsp<span>' + total + '</span>';
    var self = this;
    var currencyName = _.find(app.settings.get('1').get('options'), function(item) {return item.id === self.model.get('currency')}).value;
    return {tt: {
      heading: header,
      bodyText: '<b>' + context.y.fmt + '</b> ' + currencyName + ' (' + units + ')',
      footerText: line2
    }};
  }

});
