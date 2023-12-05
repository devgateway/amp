var param = require('jquery').param;
var _ = require('underscore');
var ChartModel = require('./chart-model-base');
var common = require('../charts/common');

module.exports = ChartModel.extend({

	defaults: {
	    xLimit: 30, //This is the max number of elements we will see in the x axis.
	    yLimit: 10, //This is the max number of elements we will see in the y axis.  
	    originalYLimit: 10, //This is the original max number of elements for the y axis (used to revert "others").
	    title: '',
	    name: '',
	    bigN: 0,
	    alternativeContainerClass: 'heatmap-chart-wrap',
	    values: [],
	    chartType: 'fragmentation',
	    swapAxes: false,
	    heatmap_type: null,
	    showResetButton: false,
	    showFullLegends: false
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
		if (_.isUndefined(data.xDataSet) || _.isUndefined(data.yDataSet)) {
			// The EP for heatmaps is different than the other charts because it returns an empty object, so we set explicitly some fields to empty value. 
			data.yDataSet = [];
			data.xDataSet = [];
			data.yDataSetIds = [];
			data.xDataSetIds = [];
			data.matrix = [];
			data.xTotals = 0;
			data.yTotals = 0;
			data.xCount = 0;
			data.yCount = 0;
			data.xTotalCount = 0;
			data.yTotalCount = 0;
		}		
		self.values.x = data.xDataSet;
		self.values.xid = data.xDataSetIds;
		self.values.y = data.yDataSet;
		self.values.yid = data.yDataSetIds;
		for (var i = 0; i < data.yDataSet.length; i++) {
			for (var j = 0; j < data.xDataSet.length; j++) {
				if (data.matrix[i] !== null) {
					var value = data.matrix[i][j] !== null ? data.matrix[i][j] : {p: -1, amount: '0'};
					var row = {y: i + 1, x: j + 1, value: value.p, amount: value.dv, yid: data.yDataSetIds[i], xid: data.xDataSetIds[j]/*, yname:
					 data.yDataSet[i],
					 xname: data.xDataSet[i]*/}; //name is for tooltip
					self.values.push(row);
				} else {
					self.values.push({y: i + 1, x: j + 1, value: -1, amount: "0", yid: data.yDataSetIds[i], xid: data.xDataSetIds[j]});
				}
			}
		}
				
		// Normalize values.
		self.values = this.normalizeValues(self.values);
		
		// Add totals data.
		self.values.xPTotals = this.normalizeValues(data.xPTotals);
		self.values.yPTotals = this.normalizeValues(data.yPTotals);
		self.values.xTotals = data.xTotals;
		self.values.yTotals = data.yTotals;
		self.values.xCount = data.xCount;
		self.values.yCount = data.yCount;
		self.values.xTotalCount = data.xTotalCount;
		self.values.yTotalCount = data.yTotalCount;	
		self.values.model = this;

		if (!this.localizedLookup) {
			// we can't procede if we don't have translations yet :(
			// this code should now be unreachable, but y'never know...
			this.app.report('Loading error', ['Translations for the application were not loaded before rendering']);
		}
		var chartName = ['amp.dashboard:chart-', this.get('name').replace(/ /g, ''), '-'].join('');

		data.processed = [{values: this.values}];
		data.values = this.values;
		
		if (data.yCount > this.get('originalYLimit') + 1) {
			this.set('showResetButton', true);
		} else {
			this.set('showResetButton', false);
		}
		
		return data;
	},
	
	normalizeValues: function(values) {
		if (_.isUndefined(values) === false) {
			for (var i = 0; i < values.length; i++) {
				var auxValue = values[i].value !== undefined ? values[i].value : values[i];
				if (auxValue > 0 && auxValue < 1) {
					//Do nothing;
				} else {
                    if (auxValue == null) {
                        auxValue = -99;
                    }
                    if (values[i].value !== undefined) {
                        values[i].value = Math.floor(auxValue);
                    } else {
                        values[i] = Math.floor(auxValue);
                    }
				}
			}
		}
		return values;
	},

	fetch: function(options) {
		var self = this;
		options = _.defaults(options || {}, { url: this.url });
		
		// Process params from heat-map/configs, in that EP we have defined each heatmap.
		var configs = this.get('heatmap_config').models[0];
		var thisChart = _.find(configs.get('charts'), function (item) {
			return item.name === self.get('name');
		});
		var xColumn = self.get('xAxisColumn') !== '' ?
			self.get('xAxisColumn') :
			_.find(configs.get('columns'), function (item, i) {
				return item.origName === thisChart.xColumns[0];
			}).origName; // First column is default.
		var yColumn = _.find(configs.get('columns'), function (item, i) {
			return item.origName === thisChart.yColumns[0];
		}).origName; // First column is default.
		
		// Check if we need to switch axis.
		if (self.get('swapAxes') === true) {
			var auxAxis = yColumn;
			yColumn = xColumn;
			xColumn = auxAxis;
		}		
		var paramsForHeatMap = {xCount: self.get('xLimit'), xColumn: xColumn, yColumn: yColumn, yCount: self.get('yLimit')};		
		var filterObject = JSON.parse(options.data);		
		paramsForHeatMap.filters = (filterObject && filterObject.filters) ? filterObject.filters : {}; 
		options.data = JSON.stringify(paramsForHeatMap);

		return ChartModel.prototype.fetch.call(this, options);
	}
});