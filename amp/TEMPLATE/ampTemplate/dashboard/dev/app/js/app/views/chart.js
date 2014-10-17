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


module.exports = BackboneDash.View.extend({

  className: function() {
    return this.model.id === 'top-regions' ? 'col-xs-12' : 'col-xs-6';
  },

  initialize: function(options) {
    this.app = options.app;
    _(this).bindAll('getChart');
  },

  render: function() {
    this.$el.html(template({ chart: this.model, loading: true, util: util }));

    this.app.tryAfter(this.model.fetch(), function() {
      this.$el.html(template({ chart: this.model, loading: false, util: util }));
      nv.addGraph(_(function() { return this.app.tryTo(this.getChart, this); }).bind(this));
    }, this);

    return this;
  },

  getChart: function() {

    var data = this.model.pick('values');

    data.values.push({
      name: 'Others',
      amount: this.model.get('total') -
        _.chain(data.values).pluck('amount').reduce(function(l, r) { return l + r; }, 0).value()
    })


    if (_(_(data.values).pluck('name')).uniq().length < data.values.length) {
      this.app.report('Data Error',
        ['The data for ' + this.model.get('name') + ' was inconsistent due to duplicate keys',
        'The chart will be shown, but it may have errors or other issues as a result.']);
    }


    var chart = nv.models.discreteBarChart()
      .x(function(d) { return d.name })    //Specify the data accessors.
      .y(function(d) { return d.amount })
      .showValues(true)
      .showYAxis(false)
      // .showXAxis(false)
      .valueFormat(util.formatKMB(3))
      .tooltipContent(_(function(a, y, b, raw) { return tooltip({
        y: y,
        raw: raw,
        d3: d3,
        currency: this.model.get('currency'),
        total: this.model.get('total')
      })}).bind(this))
      .margin({ top: 5, right: 20, bottom: 60, left: 20 })
      .transitionDuration(350);

    chart.xAxis
      .rotateLabels(15);  // because many labels are way too long

    d3.select(this.el.querySelector('svg'))
      .datum([data])
      .call(chart);

    nv.utils.windowResize(chart.update);

    return chart;
  }

});
