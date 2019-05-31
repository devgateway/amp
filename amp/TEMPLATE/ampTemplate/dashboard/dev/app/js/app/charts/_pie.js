/*
 * Drawing a pie chart in AMP? Please use ./chart.js instead.
 */

var nv = window.nv;  // nvd3 is a pain
// var d3 = require('d3-browserify');

var _ = require('underscore');
var common = require('./common');
var util = require('../../ugly/util');
var customizedPieChart = require('./customized/pieChart.js');
var customizedLegend = require('./customized/customizedLegend');


function dataToNv(data) {
  if (data.length !== 1) {
    common.fail('Pie charts can only show one series of data. got', data.length);
  }
  if (!_.isArray(data[0].values)) {
    common.fail('Data series did not have a "values" array. Instead', data[0].values);
  }
  return getPositiveValues(data[0].values);
}

function getPositiveValues(values) {
  return _.filter(values, function(e) {
    return e.y>0;
  });
}

function countCategories(data) {
  // note: this takes regular data, not dataToNv data.
  return getPositiveValues(data[0].values).length;
}


function chart(options, data) {
    var height = options.height < 400 ? 300 : 400;
    var calculatedHeight = util.calculateChartHeight(data[0].values.length, false, options.model);
    if (calculatedHeight !== null) {
        height = calculatedHeight;
    }
    var isRtl = app.generalSettings.attributes['rtl-direction'];

    var _chart = nv.models.customizedPieChart()
        .valueFormat(options.shortFormatter)
        .labelType('percent')
        .showLegend(false)
        .donut(true)
        .rtl(isRtl)
        .height(height)
        .margin({top: 5, right: 5, bottom: 5, left: 5})
        .donutRatio(0.35);
    return _chart;
}

function addLegend(svg, chart, nvData, trimLabels, width) {
	  var legendHeight;
      var isRtl = app.generalSettings.attributes['rtl-direction'];
	  var legend = nv.models.customizedLegend()
	    .width(width || svg.clientWidth)
	    .margin({left: 20, right: 20})
	    .rightAlign(isRtl)
         .rtl(isRtl)
	    .color(util.categoryColours(nvData.length))
	    .key(function(d) { return trimLabels ? util.formatShortText(12)(d.x) : util.formatShortText(85)(d.x); });

	  d3.select(svg)
	    .datum(nvData)
	    .append('g')
	      .attr('class', 'legend')
	      .datum(nvData)
	      .call(legend);

	  legendHeight = svg.querySelector('.legend').getBBox().height;
	  chart.margin({top: legendHeight + 15});
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
  addLegend: addLegend,
  removeLegend: removeLegend,
  dataToNv: dataToNv,
  chart: chart
};
