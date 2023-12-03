var _ = require('underscore');
var ChartModel = require('./chart-model-base');
var common = require('../charts/common');

module.exports = ChartModel.extend({

  defaults: {
    typed: true,
    limit: 3,
    title: '',
    stacked: false,
    seriesToExclude: [],
    yearTotals:{},
    chartType: 'fundingType'
  },

  _prepareTranslations: function() {
    var self = this;
    var ftypeBaseLanguage = {};

    /* Prepare the translations for the chart */
    var chartName = ['amp.dashboard:chart-', this.get('name').replace(/ /g, ''), '-'].join('');

    /*
     * TODO: load all the localizations in this chart's namespace to this array
     * from initial-translation-request.json -- For now just hardcode the two sorts.
     */
    if (this.get('name') === 'Funding Type') {
      ftypeBaseLanguage[chartName + 'Grant'] = 'Grant';
      ftypeBaseLanguage[chartName + 'Loan'] = 'Loan';
      ftypeBaseLanguage[chartName + 'others'] = 'Others';
    }

    this.localizedFType = this.app.translator.translateList(ftypeBaseLanguage).then(
      function(localizedKeyVal) {
        self.localizedLookup = localizedKeyVal;
      });
  },

  parse: function(data) {
	this.set('title', data.title);
	
    var self = this;
    var years;
    if (data.values.length > 0 && !isNaN(parseInt((data.values)[0].Year, 10))) {
      years = _.sortBy(data.values, function(obj) { return parseInt(obj.Year, 10); });
    } else {
      years = _(data.values).sortBy('Year');
    }

    var chartName = ['amp.dashboard:chart-', this.get('name').replace(/ /g, ''), '-'].join('');
    var localizedOthers = self.localizedLookup[chartName + 'others'];

    // reformat the data for nvd3
    data.processed = _(years)
      .chain()
      .reduce(function(series, year) {
        series.push.apply(series, _(year.values).pluck('type'));
        return series;
      }, [])
      .uniq()
      .map(function(s) {
        var cleanName = s.replace(/[ :.]/g, '');
        var localizedName = s;
        var keyId;
        if (self.localizedLookup[chartName + cleanName]) {
          localizedName = self.localizedLookup[chartName + cleanName];
        }
        return {
          key: localizedName,
          values: _(years).map(function(y) {
            var yearValue = _(y.values).findWhere({type: s});
            keyId = yearValue.id;
            return {
              x: y.Year,
              y: yearValue && yearValue.amount || 0,
              z: yearValue && yearValue.formattedAmount || 0,
            };
          }),
          disabled: (_.indexOf(self.get('seriesToExclude'),localizedName) != -1),
		  keyId: keyId
        };
      })
      .value().sort(function(a, b){
		  var x = a['key'];
		  var y = b['key'];
		  return ((x < y) ? -1 : ((x > y) ? 1 : 0));
	  });
 
	
    // group smallest contributors as "other"s
    if (this.get('limit') < data.processed.length) {
    	// Summarize each funding type and sort by total descending, create a new array only with funding types names.
    	var othersNames = _(data.processed)
	        .chain()
	        .map(function(series) {
	        	return {
	        		key: series.key,
		        	total: _(series.values).reduce(function(t, v) { return t + v.y; }, 0)
		        };
	        })
	        .sortBy('total')
	        .reverse()
	        .rest(this.get('limit'))
	        .pluck('key')
	        .value();

    	// Create a new array from the original data but only with the funding types from 'Others' category.
    	var othersSeriesValues = _(data.processed).filter(function(series) { return _(othersNames).contains(series.key); })
    	var sortedOthersSeriesValues = [];
    	
    	// Sort 'othersSeriesValues' by the values from 'othersNames' 
    	// (if we dont do this step then we will 'extract' the wrong funding type when clicking 'Others').
    	othersNames.forEach(function(key) {
    		var found = false;
    		othersSeriesValues = othersSeriesValues.filter(function(item) {
    	        if(!found && item.key === key) {
    	        	sortedOthersSeriesValues.push(item);
    	            found = true;
    	            return false;
    	        } else 
    	            return true;
    	    })
    	});
    	
    	// Summarize all funding types (from 'Others') by year.
    	othersSeriesValues = _(sortedOthersSeriesValues)
	    	.chain()
	    	.map(function(series) { return series.values; })
	        .transpose()
	        .map(function(othersYear) {
	            return {
	            	x: othersYear[0].x,
	            	y: _(othersYear).reduce(function(t, s) { return t + s.y; }, 0),
	            	z: ''
	            };
	        })
	        .map(function(item) {
	        	return {
	        		x: item.x,
	        		y: item.y,
	        		z: common.formatNumber(item.y /** parseFloat(app.generalSettings.numberDivider)*/)
	        	};
	        })
	        .value()
	    
	    var othersSeries = { 
	        key: localizedOthers,
	        color: '#777',
	        special: 'others',
	        disabled: (_.indexOf(self.get('seriesToExclude'), localizedOthers) != -1),
	        values: othersSeriesValues
	    };
    	    	// Remove from the original data the funding types we grouped in 'Others' (cant use slice because the sorting in 'data.processed' is different).
    	data.processed = _(data.processed).filter(function(item) {return !_(othersNames).contains(item.key)});
    	data.processed.push(othersSeries);
    }
    
    var yearTotals = {};
	_.each(data.processed, function(d){
		_.each(d.values, function(value){
			yearTotals[value.x] = (yearTotals[value.x] || 0) + value.y;
		});    		
	});
	
	this.set('yearTotals', yearTotals);

    return data;
  }

});
