var d3 = require('d3');
var ChartViewBase = require('./chart-view-base');
var _ = require('underscore');

module.exports = ChartViewBase.extend({

  uiDefaults: {
	adjtype: 'FAKE',
	xAxisColumn: '',
    showTotal: false,
    showMeasuresSelector: true,
    showTopLegends: false,
    showCommonChartArea: false,
    disableResize: true
  },
  
  chartViews: [
    'heatmap',
    'table'    
  ],
  
  modalView: undefined,
  
  chartClickHandler : function(context) {
	  if (context.y.fmt === app.translator.translateSync('amp.dashboard:chart-heatmap-others')) {
		  this.model.set('yLimit', this.model.get('yLimit') + 10);
		  this.updateData();
	  }
  },
  
  downloadChartOptions: {
    trimLabels: false
  }

});