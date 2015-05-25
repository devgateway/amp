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
  var units = app.translator.translateSync(app.settings.numberMultiplierDescription);

  // IMPORTANT: We assume this chart will ALWAYS show 2 data series.
  var index = -1;
  if (context.data[0].disabled === true) {
    index = 1;
  } else {
    index = context.series.index;
  }
  var header = context.x.raw + " " +
        app.translator.translateSync("amp.dashboard:aid-predictability-" +
        context.data[index].originalKey + "-" + this.model.get('measure'), "");

  var otherSeries = context.data[1 - index];  // WARNING: assumes only 2 series
  var otherHere = otherSeries.values[context.x.index];
  var line2Amount = 0;
  if(otherHere.y > 0) {
	  line2Amount = context.y.raw / otherHere.y;
  }
  var line2 = '<b>' + d3.format('%')(line2Amount) +
        '</b>&nbsp<span>' + of + '</span>&nbsp' + context.x.raw +
        '&nbsp<span>' + total + '</span>';

  return {tt: {
      heading: header,
      bodyText: '<b>' + context.y.fmt + '</b> ' + this.model.get('currency') + ' (' + units + ')',
      footerText: line2
    }};
  }

});
