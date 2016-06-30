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
	//console.log("_heatmap.dataToNv");
  return data;
}


function countCategories(data) {
	//console.log("_heatmap.countCategories");
  // note: this takes regular data, not dataToNv data.
  return data[0].values.length - 1;  // 1 for others...?
}


function chart(options, data) {
	//console.log("_heatmap.chart");
  //this check is needed because I need strictly either 300 or 400 px, and sometimes, when the chart overflows, it
  //will give me >400 px height
  var height = options.height < 400 ? 300 : 400;
  var calculatedHeight = util.calculateChartHeight(data[0].values.length, false, options.model);
  if (calculatedHeight !== null) {
	  height = calculatedHeight; 
  }
   
  //TODO: Check if discreteBarChart is the best option.
  var _chart = nv.models.heatMapChart().height(900);
    /*.valueFormat(options.shortFormatter)
    .showValues(true)
    .showYAxis(false)
    .showXAxis(false)
    .height(height)
    .margin({ top: 5, right: 10, bottom: 10, left: 10 });*/
  return _chart;
}


function addLegend(svg, chart, nvData, trimLabels, width) {
	//console.log("_heatmap.addLegend");
  /*var legendHeight;

  var legend = nv.models.legend()
    .width(width || svg.clientWidth)
    .margin({left: 20, right: 20})
    .rightAlign(false)
    .color(util.categoryColours(nvData[0].values.length))
    .key(function(d) { return trimLabels ? util.formatShortText(12)(d.x) : util.formatShortText(85)(d.x); });

  d3.select(svg)
    .datum(nvData)
    .append('g')
      .attr('class', 'legend')
      .datum(nvData[0].values)
      .call(legend);

  legendHeight = svg.querySelector('.legend').getBBox().height;
  chart.margin({top: legendHeight + 15});*/
}


module.exports = {
  dispatchName: 'heatmap',
  countCategories: countCategories,
  addLegend: addLegend,
  dataToNv: dataToNv,
  chart: chart
};
