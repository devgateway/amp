/*
 * Drawing a pie chart in AMP? Please use ./chart.js instead.
 */

var nv = window.nv;  // nvd3 is a pain
// var d3 = require('d3-browserify');

var _ = require('underscore');
var common = require('./common');


function dataToNv(data) {
  if (data.length !== 1) {
    common.fail('Pie charts can only show one series of data. got', data.length);
  }
  if (!_.isArray(data[0].values)) {
    common.fail('Data series did not have a "values" array. Instead', data[0].values);
  }
  return data[0].values;  
}

function countCategories(data) {
  // note: this takes regular data, not dataToNv data.
  return data[0].values.length;
}


function chart(options) {
  var _chart = nv.models.pieChart()
    .valueFormat(options.shortFormatter)
    .labelType('percent')
    .donut(true)
    .donutRatio(0.35);
  return _chart;
}


function removeLegend(chart) {
  chart.showLegend(false);
}


function normalizeNvTTArgs(fmtX, fmtY, raw) {
  // nv pie charts drop the seriesName as first arg
  return [void 0, fmtX, fmtY, raw];
}


module.exports = {
  dispatchName: 'pie',
  normalizeNvTTArgs: normalizeNvTTArgs,
  countCategories: countCategories,
  removeLegend: removeLegend,
  dataToNv: dataToNv,
  chart: chart
};
