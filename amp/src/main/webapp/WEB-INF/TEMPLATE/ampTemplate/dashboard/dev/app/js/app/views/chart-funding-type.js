var d3 = require('d3');
var ChartViewBase = require('./chart-view-base');
var _ = require('underscore');
var util = require('../../ugly/util');

var ProjectsListModalView = require('./chart-detail-info-modal');

module.exports = ChartViewBase.extend({

  uiDefaults: {
    big: true,
    adjtype: 'FAKE',
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
	  var stackedLegendTrn = app.translator.translateSync("amp.dashboard:filters-chart-legends-Stacked","Stacked");
	  var groupedLegendTrn = app.translator.translateSync("amp.dashboard:filters-chart-legends-Grouped","Grouped");
	  if(key == stackedLegendTrn || key == groupedLegendTrn){
		  this.model.set('stacked', (key == stackedLegendTrn ));	
	  }else{
		  var seriesToExclude = this.model.get('seriesToExclude') ? this.model.get('seriesToExclude') : [];
		  var indexOfKeyInExclusionList = _.indexOf(seriesToExclude, key);
		  if($(e.currentTarget).attr('class').indexOf('disabled') != -1){
			  if(indexOfKeyInExclusionList == -1){
				  seriesToExclude.push(key);
			  }			  
		  }else{
			  if(indexOfKeyInExclusionList != -1){
				  seriesToExclude.splice(indexOfKeyInExclusionList, 1);
			  }
		  }
		  this.model.set('seriesToExclude',seriesToExclude);
	  }	 
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
    var units = app.translator.translateSync(app.generalSettings.numberDividerDescription);

    var activeTooltipTitles = _.filter(context.data, function(series) {
      return series.disabled !== true;
    });
    
    var totalForYear = this.model.get('yearTotals') ? this.model.get('yearTotals')[context.x.raw ] : null;
    var totalSpan = ' &nbsp<span>' + total + '</span>';
    var ofTotal =  of + '</span>&nbsp' + context.x.raw + totalSpan;
    var footerText =  util.formatOfTotal(context.y.raw,totalForYear,ofTotal);

    var self = this;
    var currencyName =  app.settingsWidget.definitions.findCurrencyById(self.model.get('currency')).value;  
    return {tt: {
      heading: context.x.raw + ' ' + activeTooltipTitles[context.series.index].key,
      bodyText: '<b>' + context.y.fmt + '</b> ' + currencyName + ' (' + units + ')',
      footerText: footerText + '</span>'
    }};
  },

  chartClickHandler: function(context) {
    // clicking on the "others" bar loads five more.
    if (context.data[context.series.index].special === 'others') {
      this.model.set('limit', this.model.get('limit') + 1);
    } else {
        this.modalView = new ProjectsListModalView({app: app, context: context, model: this.model});
        this.openInfoWindow((context.x.fmt || context.x.raw) + ' ' + context.data[context.series.index].key);
    }
  }

});
