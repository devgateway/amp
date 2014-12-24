var d3 = require('d3-browserify');
var ChartViewBase = require('./chart-view-base');


module.exports = ChartViewBase.extend({

  uiDefaults: {
    big: true,
    adjtype: 'Actual Commitments',
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
    return {tt: {
      heading: context.x.raw + ' ' + context.series.key,
      bodyText: '<b>' + context.y.fmt + '</b> ' + this.model.get('currency'),
      footerText: '<b>' + d3.format('%')(context.y.raw / this.model.get('total')) +
        '</b> of ' + context.x.raw + ' total'
    }};
  },

  chartClickHandler: function(context) {
    // clicking on the "others" bar loads five more.
    if (context.data[context.series.index].special === 'others') {
      this.model.set('limit', this.model.get('limit') + 1);
    }
  }

});
