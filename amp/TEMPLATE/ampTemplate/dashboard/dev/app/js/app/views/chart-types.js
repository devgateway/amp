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
        if (!model.get('typed')) {
          chart.showControls(false);  // disable grouped/stacked toggle
        }
        chart.tooltipContent(function(seriesName, year, fmtValue, raw) {
          var others;
          if (!model.get('typed')) {
            var o = model.get('processed')[1 - raw.seriesIndex];
            others = { name: o.key, total: o.values[raw.pointIndex].y };
          } else {
            others = {
              name: 'total',
              total: _(model.get('processed')).reduce(function(t, s) {
                return t + s.values[raw.pointIndex].y;
              }, 0)
            };
          }
          return predictabilityTooltip({
            year: year,
            seriesName: seriesName,
            value: d3.format(',')(Math.round(raw.value)),
            otherSeriesName: others.name,
            percent: d3.format('%')(raw.value / others.total),
            currency: model.get('currency')
          });
        });
        drawCommon(chart, data, categories, container, function() {
          d3.selectAll(container.querySelectorAll('.nv-bar'))
            .on('click', function(d) {
              if (model.get('processed').length - 1 === d.series &&
                  model.get('processed')[d.series].color === '#777') {
                model.set('limit', model.get('limit') + 1);
              }
            });
        });
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
