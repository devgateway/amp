var d3 = require('d3');
var ChartViewBase = require('./chart-view-base');
var ModalView = require('./chart-tops-info-modal');
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
  
  //Dont try to call initialize here because it throws a 'Module initialization error' :((
  /*initialize: function(options) {
	  this.modalView = new ModalView({ app: options.app, collection: this.model.collection });
  },*/

  downloadChartOptions: {
    trimLabels: false
  }

});
