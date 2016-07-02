var param = require('jquery').param;
var _ = require('underscore');
var ChartModel = require('./chart-model-base');
var common = require('../charts/common');

module.exports = ChartModel.extend({

	defaults: {
	    xLimit: 31,
	    yLimit: 10,
	    title: '',
	    name: '',
	    bigN: 0,
	    alternativeContainerClass: 'heatmap-chart-wrap',
	    values: [],
	    chartType: 'fragmentation'
	},

	_prepareTranslations: function() {
		var topBaseLanguage = {};

	    /* Prepare the translations for the chart */
	    var chartName = ['amp.dashboard:chart-', this.get('name').replace(/ /g, ''), '-'].join('');
	
	    this.localizedTopChart = this.app.translator.translateList(topBaseLanguage)
	    	.done(_(function(localizedTopChartKeyVal) {
	    		this.localizedLookup = localizedTopChartKeyVal;
	    	}).bind(this));
	},

	parse: function (data) {
		var self = this;
		self.values = new Array();
		self.values.x = data.xDataSet;
		self.values.y = data.yDataSet;
		for (var i = 0; i < data.yDataSet.length; i++) {					
			for (var j = 0; j < data.xDataSet.length; j++) {
				if (data.matrix[i] !== null) {
					var value = data.matrix[i][j] !== null ? data.matrix[i][j] : {p: -1, amount: '0'};
					var row = {y: i + 1, x: j + 1, value: value.p, amount: value.dv/*, yname: data.yDataSet[i], xname: data.xDataSet[i]*/}; //name is for tooltip
					self.values.push(row);
				} else {
					self.values.push({y: i + 1, x: j + 1, value: -1, amount: "0"});
				}
			}
		}
				
		// Normalize values.
		// TODO: recalculate column percentages to sum 100%.
		self.values = this.normalizeValues(self.values);
		
		// Add totals data.
		self.values.xPTotals = this.normalizeValues(data.xPTotals);
		self.values.yPTotals = this.normalizeValues(data.yPTotals);
		self.values.xTotals = data.xTotals;
		self.values.yTotals = data.yTotals;

		if (!this.localizedLookup) {
			// we can't procede if we don't have translations yet :(
			// this code should now be unreachable, but y'never know...
			this.app.report('Loading error', ['Translations for the application were not loaded before rendering']);
		}
		var chartName = ['amp.dashboard:chart-', this.get('name').replace(/ /g, ''), '-'].join('');

		data.processed = [{values: this.values}]; //TODO: processed???
		data.values = this.values;
		//console.log(data);
		return data;
	},
	
	normalizeValues: function(values) {
		for (var i = 0; i < values.length; i++) {
			var auxValue = values[i].value !== undefined ? values[i].value : values[i]; 
			if (auxValue > 0 && auxValue < 1) {
				//self.values[i].value = 1;
			} else {
				if (values[i].value !== undefined) {
					values[i].value = Math.floor(auxValue);
				} else {
					values[i] = Math.floor(auxValue);
				}
			}			
		}
		return values;
	},

	fetch: function(options) {
		//TODO: add code for saved dashboards!!!		
		var self = this;
		options = _.defaults(options || {}, { url: this.url + '?' + param(this.pick('xLimit')) });
		
		// Process params from heat-map/configs, in that EP we have defined each heatmap.
		var configs = this.get('heatmap_config').models[0];
		var thisChart = _.find(configs.get('charts'), function(item) {return item.name === self.get('name')});
		var xColumn = self.get('xAxisColumn') !== '' ? self.get('xAxisColumn') : configs.get('columns')[thisChart.xColumns[0]].origName; // First column is default.
		var yColumn = configs.get('columns')[thisChart.yColumns[0]]; // First column is default.
		var paramsForHeatMap = {xCount: self.get('xLimit'), xColumn: xColumn, yColumn: yColumn.origName}; 		
		//options.data = JSON.stringify($.extend({}, paramsForHeatMap, JSON.parse(options.data)));
		paramsForHeatMap.filters =  JSON.parse(options.data);
		options.data = JSON.stringify(paramsForHeatMap);

		return ChartModel.prototype.fetch.call(this, options);
	}
});