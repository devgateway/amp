var d3 = require('d3');
var ChartViewBase = require('./chart-view-base');
var _ = require('underscore');
var util = require('../../ugly/util');

var ProjectsListModalView = require('./chart-detail-info-modal');

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

  modalView: undefined,

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
    var units = app.translator.translateSync(app.generalSettings.numberDividerDescription);

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
    var ofTotal = of + ' '  + context.x.raw + ' ' + total;

    var otherSeries = context.data[1 - index];  // WARNING: assumes only 2 series
    var otherHere = otherSeries.values[context.x.index];

    var line2 = util.formatOfTotal(context.y.raw, otherHere.y, ofTotal);


    var self = this;
    var currencyName = app.settingsWidget.definitions.findCurrencyById(self.model.get('currency')).value; 
    return {tt: {
      heading: header,
      bodyText: '<b>' + context.y.fmt + '</b> ' + currencyName + ' (' + units + ')',
      footerText: line2
    }};
  },

    getNiceContext: function (e) {

        var x = e.data[e.series.index].values[e.x.index].x,
            y = e.data[e.series.index].key;

        if (x == undefined || y == undefined) {
            return null;
        }
        return {
            data: e.data,
            series: e.series,
            x: {
                raw: x,
                fmt: x,
                index: x
            },
            y: {
                raw: y,
                fmt: y,
                index: y
            }
        };
    },

    chartClickHandler: function (e) {
        var self = this;
        var context = self.getNiceContext(e);
        if (context){
            this.modalView = new ProjectsListModalView({app: app, context: context, model: this.model});
            this.openInfoWindow((context.x.fmt || context.x.raw) + ' ' + context.series.key);
        }
    }

});
