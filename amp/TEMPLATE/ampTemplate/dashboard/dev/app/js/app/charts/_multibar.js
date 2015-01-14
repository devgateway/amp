/*
 * Drawing a multibar chart in AMP? Please use ./chart.js instead.
 */

var nv = window.nv;  // nvd3 is a pain
// var d3 = require('d3-browserify');


function dataToNv(data) {
  return data;
}


function countCategories(data) {
  // note: this takes regular data, not dataToNv data.
  return data.length;
}


function staggerX(chart, nvData) {
  if (!nvData[0]) { return; }
  if (nvData[0].values.length < 5) { return; }
  chart
    .staggerLabels(true)
    .margin({bottom: 40});
}


function chart(options) {
  var maxValue = 10;
  var _chart = nv.models.multiBarChart()
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
  staggerX: staggerX,
  chart: chart
};
