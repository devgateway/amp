var fs = require('fs');
var _ = require('underscore');
var nv = window.nv;  // nvd3 is a pain
var d3 = require('d3');
var util = require('../../ugly/util');
var topsTooltip = _.template(fs.readFileSync(
  __dirname + '/../templates/tooltip-tops.html', 'UTF-8'));
var predictabilityTooltip = _.template(fs.readFileSync(
  __dirname + '/../templates/tooltip-predictability.html', 'UTF-8'));


var BAR_MARGIN = { top: 5, right: 10, bottom: 10, left: 10 };


var drawCommon = function(chart, data, categories, container, cb) {
  chart
    .color(util.categoryColours(categories));
  d3.select(container)
    .datum(data)
    .call(chart);
  nv.utils.windowResize(chart.update);
  nv.addGraph(function() { return chart; }, cb);
};


module.exports = {

  bar: function() {
    var chart = nv.models.discreteBarChart()
      .x(function(d) { return d.name; })    //Specify the data accessors.
      .y(function(d) { return d.amount; })
      .valueFormat(util.formatKMB(3))
      .showValues(true)
      .showYAxis(false)
      .showXAxis(false)
      .transitionDuration(150)
      .margin(BAR_MARGIN);

    return {
      draw: function(container, model) {
        var data = [model.get('processed')],
            categories = data[0].values.length - 1;
        chart.tooltipContent(function(a, y, b, raw) {
          return topsTooltip({
            label: raw.point.name,
            value: d3.format(',')(Math.round(raw.value)),
            currency: model.get('currency'),
            percent: d3.format('%')(raw.value / model.get('total'))
          });
        });
        drawCommon(chart, data, categories, container, function() {
          d3.selectAll(container.querySelectorAll('g:last-child > .nv-bar'))
            .on('click', function(d) {
              // clicking on the "others" bar loads five more.
              if (d.color === '#777') { model.set('limit', model.get('limit') + 5); }
            });
        });
      }
    };
  },

  pie: function() {
    var chart = nv.models.pieChart()
      .x(function(d) { return d.name; })    //Specify the data accessors.
      .y(function(d) { return d.amount; })
      .valueFormat(util.formatKMB(3))
      .labelType('percent')
      .donut(true)
      .donutRatio(0.35);

    return {
      draw: function(container, model) {
        var data = model.get('processed').values,
            categories = data.length;
        chart.tooltipContent(function(a, y, raw) {
          return topsTooltip({
            label: raw.point.name,
            value: d3.format(',')(Math.round(raw.value)),
            currency: model.get('currency'),
            percent: d3.format('%')(raw.value / model.get('total'))
          });
        });
        drawCommon(chart, data, categories, container);
      }
    };
  },

  multibar: function() {
    var chart = nv.models.multiBarChart()
      .showControls(false)  // disable grouped/stacked toggle
      .reduceXTicks(false)
      .margin(_(BAR_MARGIN).extend({ left: 40, bottom: 20 }))
      .transitionDuration(150);
    chart.yAxis
      .tickFormat(util.formatKMB(3))
      .showMaxMin(false);
    return {
      draw: function(container, model) {
        var data = model.get('processed'),
            categories = data.length;
        chart.tooltipContent(function(seriesName, year, fmtValue, raw) {
          var otherSeries = model.get('processed')[1 - raw.seriesIndex];
          return predictabilityTooltip({
            year: year,
            seriesName: seriesName,
            value: d3.format(',')(Math.round(raw.value)),
            otherSeriesName: otherSeries.key,
            percent: d3.format('%')(raw.value / otherSeries.values[raw.pointIndex].y),
            currency: model.get('currency')
          });
        });
        drawCommon(chart, data, categories, container);
      }
    };
  },

  table: function() {
    return {
      draw: function() {  // container, model) {
        // TODO: hide the SVG and throw in a bootstrap table
      }
    };
  }

};
