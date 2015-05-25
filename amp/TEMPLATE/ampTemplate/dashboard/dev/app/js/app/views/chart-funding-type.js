var d3 = require('d3-browserify');
var ChartViewBase = require('./chart-view-base');


module.exports = ChartViewBase.extend({

  uiDefaults: {
    big: true,
    adjtype: 'FAKE',
    view: 'multibar'
  },

  chartViews: [
    'multibar',
    'table'
  ],

  downloadChartOptions: {
    trimLabels: false
  },

  getTTContent: function(context) {
	var of = app.translator.translateSync("amp.dashboard:of","of");
	var total = app.translator.translateSync("amp.dashboard:total","total");
	var units = app.translator.translateSync(app.settings.numberMultiplierDescription);
    return {tt: {
      heading: context.x.raw + ' ' + context.series.key,
      bodyText: '<b>' + context.y.fmt + '</b> ' + this.model.get('currency') + ' (' + units + ')',
      footerText: '<b>' + d3.format('%')(context.y.raw / this.model.get('total')) + '</b>&nbsp<span>' + of + '</span>&nbsp' + context.x.raw + '&nbsp<span>' + total + '</span>'
    }};
  },

  chartClickHandler: function(context) {
    // clicking on the "others" bar loads five more.
    if (context.data[context.series.index].special === 'others') {
      this.model.set('limit', this.model.get('limit') + 1);
    }
  }

});
