/*
 * Drawing a bar chart in AMP? Please use ./chart.js instead.
 */

var barDebug = require('../../../../../../../reamp/tools/log')("amp:dashboards:charts:bar");

var nv = window.nv;  // nvd3 is a pain
var d3 = require('d3');
var util = require('../../ugly/util');
var heatMapChart = require('./customized/heatMapChart');
var _ = require('underscore');

function dataToNv(data) {
  return data;
}


function countCategories(data) {
  // note: this takes regular data, not dataToNv data.
  return data[0].values.length - 1;  // 1 for others...?
}


function chart(options, data) {
  //this check is needed because I need strictly either 300 or 400 px, and sometimes, when the chart overflows, it
  //will give me >400 px height
  var height = options.height < 400 ? 300 : 400;
  var calculatedHeight = util.calculateChartHeight(data[0].values.length, false, options.model);
  if (calculatedHeight !== null) {
	  height = calculatedHeight; 
  }
   
  var _chart = nv.models.heatMapChart().height(900);
  return _chart;
}


function addLegend(svg, chart, nvData, trimLabels, width) {
}


module.exports = {
  dispatchName: 'heatmap',
  countCategories: countCategories,
  addLegend: addLegend,
  dataToNv: dataToNv,
  chart: chart
};
