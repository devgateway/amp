var fs = require('fs');
var _ = require('underscore');
var nv = window.nv;  // nvd3 is a pain
var d3 = require('d3');
var BackboneDash = require('../backbone-dash');
var util = require('../../ugly/util');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/chart.html', 'UTF-8'));
var tooltip = _.template(fs.readFileSync(
  __dirname + '/../templates/tooltip.html', 'UTF-8'));


var chartCommon = function(chart) {
  chart
    .x(function(d) { return d.name; })    //Specify the data accessors.
    .y(function(d) { return d.amount; })
    .valueFormat(util.formatKMB(3))

  return function(el, model) {
    // things we can only compute after we have data for the charts
    chart
      .color(util.categoryColours(model.get('values').length))
      .tooltipContent(_(function(a, y, b, raw) {
        return tooltip({
          y: y,
          raw: (typeof raw === 'function') ? b : raw,
          d3: d3,
          currency: model.get('currency'),
          total: model.get('total')
        });
      }).bind(this))

    d3.select(el)
      .datum(model.get('view') === 'bar' ?
        [model.get('processed')] :
        model.get('processed').values)
      .call(chart);

    nv.utils.windowResize(chart.update);
    nv.addGraph(function() { return chart; });
  };
};


var charts = {

  bar: chartCommon(
    nv.models.discreteBarChart()
      .showValues(true)
      .showYAxis(false)
      .showXAxis(false)
      .transitionDuration(150)
      .margin({ top: 5, right: 10, bottom: 10, left: 10 }) ),

  pie: chartCommon(
    nv.models.pieChart()
      .labelType('percent')
      .donut(true)
      .donutRatio(0.35) )
};


module.exports = BackboneDash.View.extend({

  uiDefaults: {
    embiggen: false,
    view: 'bar'
  },

  events: {
    'change .dash-adj-type input': 'changeAdjType',
    'click .chart-view': 'changeChartView',
    'click .expand': 'embiggen'
  },

  initialize: function(options) {
    this.app = options.app;
    this.model.set(this.uiDefaults);
    // TODO: load any state we need
    this.chart = charts.bar;  // bar, etc.
    this.listenTo(this.model, 'change:adjtype', this.render);
  },

  render: function() {
    var renderOptions = {
      chart: this.model,
      util: util,
      loading: true
    };
    this.$el.html(template(renderOptions));

    this.app.tryAfter(this.model.fetch(), function() {
      this.$el.html(template(_(renderOptions).extend({ loading: false })));
      this.chart(this.el.querySelector('.dash-chart'), this.model);
    }, this);

    return this;
  },

  changeAdjType: function(e) {
    var newType = e.currentTarget.dataset.acad;
    this.model.set('adjtype', newType);
  },

  changeChartView: function(e) {
    var view = e.currentTarget.dataset.view;
    this.model.set('view', view);
    this.chart = charts[view];
    this.render();
  },

  embiggen: function() {
    // toggle big/small charts on large screens
    this.model.set('embiggen', !this.model.get('embiggen'));
    this.render();
  },

  setClear: function(shouldBreak) {
    // layout hints, should only be called by ./charts.js
    this.$el[shouldBreak ? 'addClass' : 'removeClass']('clearfix');
  }

});
