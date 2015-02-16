/*
 * Drawing a multibar chart in AMP? Please use ./chart.js instead.
 */

var nv = window.nv;  // nvd3 is a pain
var customizedMultiBarChart = require('./customized/multiBarChart.js');
// var d3 = require('d3-browserify');


function dataToNv(data) {
  return data;
}


function countCategories(data) {
  // note: this takes regular data, not dataToNv data.
  return data.length;
}


function chart(options) {
  var maxValue = 10;
  var _chart = nv.models.customizedMultiBarChart()  
    .forceY([0, maxValue])  // ensures yAxis is showing at least 0 and 10, but won't restrict the domain
                            // (meaning if the are values falling outside the range it will show then).
    .reduceXTicks(false)
    .margin({ top: 5, right: 10, bottom: 20, left: 50 });

  if (!options.nvControls) {
    _chart.showControls(false);
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
