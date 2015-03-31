var d3 = require('d3-browserify');
var ChartViewBase = require('./chart-view-base');


module.exports = ChartViewBase.extend({

  uiDefaults: {
    big: false,
    view: 'multibar'
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
	var of = app.translator.translateSync("amp.dashboard:of","of");
	var total = app.translator.translateSync("amp.dashboard:total","total");
	var header = context.x.raw + " " + app.translator.translateSync("amp.dashboard:aid-predictability-" 
			+ context.data[context.series.index].originalKey + "-" + this.model.get('measure'), "");
    var otherSeries = context.data[1 - context.series.index],  // WARNING: assumes only 2 series
        otherHere = otherSeries.values[context.x.index],
        line2;
    if (otherHere.y > 0) {
      line2 = '<b>' + d3.format('%')(context.y.raw / otherHere.y) + '</b>&nbsp<span>' + of + '</span>&nbsp' + context.x.raw + '&nbsp<span>' + total + '</span>';
    } else {
      line2 = context.x.raw + ' ' + otherSeries.key + ': <b>' + otherHere.y + '</b>';
    }
    return {tt: {
      heading: header,
      bodyText: '<b>' + context.y.fmt + '</b> ' + this.model.get('currency'),
      footerText: line2
    }};
  }

});
