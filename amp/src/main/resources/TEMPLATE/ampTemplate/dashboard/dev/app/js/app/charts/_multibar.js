/*
 * Drawing a multibar chart in AMP? Please use ./chart.js instead.
 */

var multibarDebug = require('../../../../../../../reamp/tools/log')("amp:dashboards:charts:multibar");

var nv = window.nv;  // nvd3 is a pain
var customizedMultiBarChart = require('./customized/multiBarChart.js');
const util = require("../../ugly/util");
// var d3 = require('d3-browserify');


function dataToNv(data) {
  return data;
}


function countCategories(data) {
  // note: this takes regular data, not dataToNv data.
  return data.length;
}


function chart(options, data) {
  var maxValue = 10;
    var isRtl = app.generalSettings.attributes['rtl-direction'];
  //this check is needed because I need strictly either 300 or 400 px, and sometimes, when the chart overflows, it
  //will give me >400 px height
  var height = options.height < 400 ? 300 : 400;
  var calculatedHeight = util.calculateChartHeight(data[0].values.length, false, options.model);
  if (calculatedHeight !== null) {
    height = calculatedHeight;
  }
  multibarDebug.log("Setting multibar height", height);
  var _chart = nv.models.customizedMultiBarChart()  
    .forceY([0, maxValue])  // ensures yAxis is showing at least 0 and 10, but won't restrict the domain
                            // (meaning if the are values falling outside the range it will show then).
    .reduceXTicks(false)
    .height(height)
      .rtl(isRtl)
      .rigthAlign(isRtl)
      .rightAlignYAxis(isRtl)
    .margin({ top: 5, right: 10, bottom: 20, left: 50 });

  if (!options.nvControls) {
    _chart.showControls(false);
  }
  
  if(options.stacked){
	  _chart.multibar.stacked(true); 
  }else{
	  _chart.multibar.stacked(false);
  }

  _chart.yAxis
    .tickFormat(options.shortFormatter)
    .showMaxMin(false);

  return _chart;
}


module.exports = {
  dispatchName: 'multibar',
  countCategories: countCategories,
  dataToNv: dataToNv,
  chart: chart
};
