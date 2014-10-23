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
  // var el = document.createElement('svg');

  chart
    .x(function(d) { return d.name; })    //Specify the data accessors.
    .y(function(d) { return d.amount; })
    .valueFormat(util.formatKMB(3));
    // .elementClick(function() { console.log('elcl'); });

  return {
    draw: function(container, model) {
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
        }).bind(this));

      d3.select(container)
        .datum(model.get('view') === 'bar' ?
          [model.get('processed')] :
          model.get('processed').values)
        .call(chart);

      nv.utils.windowResize(chart.update);
      nv.addGraph(function() { return chart; });
    }
  };
};


var charts = {

  bar: function() { return chartCommon(
    nv.models.discreteBarChart()
      .showValues(true)
      .showYAxis(false)
      .showXAxis(false)
      .transitionDuration(150)
      .margin({ top: 5, right: 10, bottom: 10, left: 10 }));
  },

  pie: function() {
    var chart = nv.models.pieChart()
      .labelType('percent')
      .donut(true)
      .donutRatio(0.35);
    return chartCommon(chart);
  },

  table: function() {
    return {
      draw: function(container, model) {
        // TODO: hide the SVG and throw in a bootstrap table
      }
    };
  }

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
    this.chart = charts[this.model.get('view')]();  // bar, etc.
    this.listenTo(this.model, 'change:adjtype', this.updateData);
  },

  render: function() {
    var renderOptions = {
      model: this.model,
      chart: this.chartEl,
      util: util
    };
    this.$el.html(template(renderOptions));
    this.updateData();

    return this;
  },

  updateData: function() {
    var loading = this.$('.dash-loading');
    loading.fadeIn(100);
    this.model.fetch()
      .always(function() {
        loading.stop().fadeOut(200);
      })
      .done(_(function() {
        this.chart.draw(this.$('.dash-chart')[0], this.model);
      }).bind(this))
      .fail(_(function() {
        console.error('failed loading chart :(', arguments);
        this.app.report('Loading chart failed',
          ['There was an issue with your connection to the database, ' +
           'or the database may have crashed.']);
        this.$('svg').hide();
      }).bind(this));
  },

  changeAdjType: function(e) {
    var newType = e.currentTarget.dataset.acad;
    this.model.set('adjtype', newType);
  },

  changeChartView: function(e) {
    var view = e.currentTarget.dataset.view;
    this.model.set('view', view);
    this.chart = charts[view]();
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
