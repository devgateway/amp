var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var chartUtils = require('../../../libs/local/chart-util');
var Constants = require('../../../libs/local/constants');
var Template = fs.readFileSync(__dirname + '/legend-item-indicator-join.html', 'utf8');



module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-indicatorjoin',
  initialize: function(options) {
    this.app = options.app;
    _.bindAll(this, 'getLegendValues');
  },
  MAX_VALUE: 1000,//maximum value displayed without KMB formating
  getLegendValues: function(model,bucket){
	  if(_.isUndefined(model.maxValue) || _.isUndefined(model.minValue) || _.isUndefined(model.valuesAreIntegers)){
		  model.analyzeValues();
	  }
	  
	  var values = {};
	  values.isPercent = false;
	  var ratioOtherIndicator = this.app.data.indicatorTypes.findWhere({'value': Constants.INDICATOR_TYPE_RATIO_OTHER});
	  var percentIndicator = this.app.data.indicatorTypes.findWhere({'value': Constants.INDICATOR_TYPE_RATIO_PERCENTAGE});
	  
	  if(model.get('gapAnalysis') !== true && ((percentIndicator && percentIndicator.get('id') === model.get('indicatorTypeId')) || (ratioOtherIndicator && ratioOtherIndicator.get('id') === model.get('indicatorTypeId')))) {       
		  values.min = chartUtils.formatPercentage()(bucket.get('value')[0]);
		  values.max = chartUtils.formatPercentage()(bucket.get('value')[1]);
		  values.isPercent = true;
	  }else { 
		//do not format using KMB if all values are integers and less than or equal to 1000 or values are between 0 and 1 inclusive		  
		  if((model.valuesAreIntegers && model.maxValue <= this.MAX_VALUE) || (model.maxValue <= 1 && model.minValue >= 0) ){
		  	  values.min = bucket.get('value')[0]; 
			  values.max =  bucket.get('value')[1];			  
		  } else {          
			  values.min = chartUtils.formatKMB()(bucket.get('value')[0]);
			  values.max = chartUtils.formatKMB()(bucket.get('value')[1]);
		  }
	  } 
	  return values;
  },  
  render: function() {
    var self = this;
    self.model.load().then(function() {

      if (self.model.palette) {
        /* Use translator promise in this legend to ensure translator loads in time too--
         * extra check is needed here but not on others because
         * this legend is the default fallthrough of its parent class
         * and it gets called several times in the page load.
         */    	 
        self.app.translator.promise.then(function() {
          self.app.translator.translateDOM(
            self.template(_.extend({}, self.model.toJSON(), {
              status: 'loaded',
              colourBuckets: self.model.palette.colours,
              unit:  self.model.get('unit'),
              util: chartUtils,
              model: self.model,
              indicatorTypes: self.app.data.indicatorTypes,
              Constants: Constants,
              getLegendValues:self.getLegendValues
            }
          ))).then(function(legend) {
            self.$el.html(legend);
          });
        });

      }
    });

    return this;
  }
});
