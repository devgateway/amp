var d3 = require('d3');
var ChartViewBase = require('./chart-view-base');
var _ = require('underscore');


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
    var app = this.app;
    var of = app.translator.translateSync('amp.dashboard:of','of');
    var total = app.translator.translateSync('amp.dashboard:total','total');
    var units = app.translator.translateSync(app.settings.numberMultiplierDescription);

    var activeTooltipTitles = _.filter(context.data, function(series) {
      return series.disabled !== true;
    });

    var d3FormatTotal = d3.format('%')(context.y.raw / this.model.get('total'));
    var totalSpan = '&nbsp<span>' + total + '</span>';

    return {tt: {
      heading: context.x.raw + ' ' + activeTooltipTitles[context.series.index].key,
      bodyText: '<b>' + context.y.fmt + '</b> ' + this.model.get('currency') + ' (' + units + ')',
      footerText: '<b>' + d3FormatTotal + '</b>&nbsp<span>' + of + '</span>&nbsp' + context.x.raw + totalSpan
    }};
  },

  chartClickHandler: function(context) {
    // clicking on the "others" bar loads five more.
    if (context.data[context.series.index].special === 'others') {
      this.model.set('limit', this.model.get('limit') + 1);
    }
  }

});
