var _ = require('underscore');
var BackboneDash = require('../backbone-dash');

var Tops = require('../models/chart-tops');
var Predictability = require('../models/chart-aid-predictability');
var FundingType = require('../models/chart-funding-type');
var HeatMapChart = require('../models/chart-heatmaps');

var TopsChartView = require('./chart-tops');
var HeatMapChartView = require('./chart-heatmaps');
var PredictabilityChartView = require('./chart-aid-predictability');
var FundingTypeChartView = require('./chart-funding-type');


module.exports = BackboneDash.View.extend({

  className: 'row',

  initialize: function(options) {
    this.app = options.app;
    this.chartViews = this.collection.map(function(chart) {
      var ChartView = chart instanceof Tops ? TopsChartView
                    : chart instanceof Predictability ? PredictabilityChartView
                    : chart instanceof FundingType ? FundingTypeChartView
                    : chart instanceof HeatMapChart ? HeatMapChartView
                    : null;
      return new ChartView({ model: chart, app: this.app });
    }, this);
    this.listenToOnce(this.app.filter, 'apply', this.applyFilter);
    this.listenTo(this.collection, 'change:big', this.injectBreaks);
    this.listenTo(this.collection, 'change:bigN', this.redrawContainer);
  },

  render: function() {
    this.$el.html(_(this.chartViews).map(function(view) {
      return view.render().el;
    }));
    this.injectBreaks();
    return this;
  },

  injectBreaks: function(chartModel) {
    _(this.chartViews).reduce(function(breakAfter, thisView) {
      thisView.setClear(breakAfter);
      if (!breakAfter) {
        return thisView.model.get('big') ? false : true;
      } else {
        return false;
      }
    }, false);

    if (chartModel) {
      var chartView = _(this.chartViews).find(function(v) {
        return v.model === chartModel;
      });
      if (chartView) { chartView.render(); }
    }
  },
  
  redrawContainer: function(chartModel) {
	  if (chartModel) {
		  var chartView = _(this.chartViews).find(function(v) {
			  return v.model === chartModel;
	      });
	      if (chartView) { 
	    	  chartView.render(); 
	      }
	  } 
  }

});
