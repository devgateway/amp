var d3 = require('d3');
var ChartViewBase = require('./chart-view-base');
var ProjectsListModalView = require('./chart-detail-info-modal');
var _ = require('underscore');
var util = require('../../ugly/util');


module.exports = ChartViewBase.extend({

  uiDefaults: {
    adjtype: 'FAKE',
    showTotal: true,
    showMeasuresSelector: true,
    showTopLegends: true,
    showCommonChartArea: true    
  },
  
  chartViews: [
	'bar',
    'pie',
    'table'    
  ],
  
  modalView: undefined,
  
  //Dont try to call initialize here because it throws a 'Module initialization error' :((
  /*initialize: function(options) {
	  this.modalView = new ModalView({ app: options.app, collection: this.model.collection });
  },*/

  downloadChartOptions: {
    trimLabels: false
  },

  getTTContent: function(context) {
	var ofTotal = app.translator.translateSync("amp.dashboard:of-total","of total");
	var units = app.translator.translateSync(app.generalSettings.numberDividerDescription);
    var self = this;
    
    var currencyName =  app.settingsWidget.definitions.findCurrencyById(self.model.get('currency')).value;
      var ofTotal = app.translator.translateSync("amp.dashboard:of-total", "of total");

    var percentage = util.formatOfTotal(context.y.raw,this.model.get('totalPositive'), ofTotal);

    return {tt: {
      heading: context.x.raw,
      bodyText: '<b>' + context.y.fmt + '</b> ' + currencyName + ' (' + units + ')',
      footerText:  percentage + '</span>'
    }};
  },

  chartClickHandler: function(context) {	  
    // clicking on the "others" bar loads five more.
    if (context.data[context.series.index]
               .values[context.x.index].special === 'others') {
        this.model.set('limit', this.model.get('limit') + 5);
        this.model.set('big', true);      
    } else {
    	this.modalView = new ProjectsListModalView({ app: app, context: context, model: this.model });
    	this.openInfoWindow((context.x.fmt || context.x.raw));
    }
  }

});
