var d3 = require('d3');
var ChartViewBase = require('./chart-view-base');
var _ = require('underscore');


module.exports = ChartViewBase.extend({

  uiDefaults: {
    big: true,
    adjtype: 'FAKE',
    view: 'multibar'
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
    var units = app.translator.translateSync(app.settings.numberMultiplierDescription);

    var activeTooltipTitles = _.filter(context.data, function(series) {
      return series.disabled !== true;
    });

    var totalForYear = this.model.get('yearTotals') ? this.model.get('yearTotals')[context.x.raw ] : null;
    var d3FormatTotal = '', totalSpan = '';
    if(totalForYear && totalForYear != 0){
    	d3FormatTotal = d3.format('%')(context.y.raw / totalForYear);
        totalSpan = '&nbsp<span>' + total + '</span>';
    }

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
