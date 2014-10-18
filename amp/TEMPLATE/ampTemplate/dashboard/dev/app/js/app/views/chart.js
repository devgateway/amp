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

  events: {
    'change .dash-adj-type input': 'changeType',
    'click .expand': 'embiggen'
  },

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.model, 'change:adjType', this.render);
    _(this).bindAll('getChart');
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
      nv.addGraph(_(function() { return this.app.tryTo(this.getChart, this); }).bind(this));
    }, this);

    return this;
  },

  getChart: function() {

    var data = this.model.pick('values');

    data.values.push({
      name: 'Others',
      amount: this.model.get('total') -
        _.chain(data.values).pluck('amount').reduce(function(l, r) { return l + r; }, 0).value(),
      color: '#777'
    });


    if (_(_(data.values).pluck('name')).uniq().length < data.values.length) {
      this.app.report('Data Error',
        ['The data for ' + this.model.get('name') + ' was inconsistent due to duplicate keys',
        'The chart will be shown, but it may have errors or other issues as a result.']);
    }


    var chart = nv.models.discreteBarChart()
      .x(function(d) { return d.name; })    //Specify the data accessors.
      .y(function(d) { return d.amount; })
      .color(util.categoryColours(data.values.length))
      .showValues(true)
      .showYAxis(false)
      .showXAxis(false)
      .valueFormat(util.formatKMB(3))
      .tooltipContent(_(function(a, y, b, raw) { return tooltip({
        y: y,
        raw: raw,
        d3: d3,
        currency: this.model.get('currency'),
        total: this.model.get('total')
      }); }).bind(this))
      .margin({ top: 5, right: 10, bottom: 10, left: 10 })
      .transitionDuration(150);

    d3.select(this.el.querySelector('svg'))
      .datum([data])
      .call(chart);

    nv.utils.windowResize(chart.update);

    return chart;
  },

  changeType: function(e) {
    var newType = e.currentTarget.dataset.acad;
    this.model.set('adjType', newType);
  },

  embiggen: function() {
    this.model.set('embiggen', !this.model.get('embiggen'));
    this.render();
  },

  setClear: function(shouldBreak) {
    this.$el[shouldBreak ? 'addClass': 'removeClass']('clearfix');
  }

});
