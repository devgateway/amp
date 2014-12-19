var d3 = require('d3-browserify');
var ChartViewBase = require('./chart-view-base');


module.exports = ChartViewBase.extend({

  uiDefaults: {
    big: false,
    adjtype: 'Actual Commitments',
    view: 'bar'
  },

  downloadChartOptions: {
    trimLabels: false
  },

  getTTContent: function(context) {
    return {tt: {
      heading: context.x.raw,
      bodyText: '<b>' + context.y.fmt + '</b> ' + this.model.get('currency'),
      footerText: '<b>' + d3.format('%')(context.y.raw / this.model.get('total')) +
        '</b> of total'
    }};
  },

  chartClickHandler: function(context) {
    // clicking on the "others" bar loads five more.
    if (context.x.raw === this.model.localizedOthers &&
        context.x.index === context.series.values.length - 1) {
      this.model.set('limit', this.model.get('limit') + 5);
      if (this.model.get('limit') > 10) {  // also make the chart bigger if lots of bars are shown
        this.model.set('big', true);
      }
    }
  }


});
