var fs = require('fs');
var _ = require('underscore');

// nv attaches to the window global namespace; it doesn't export anything
require('nvd3');
var nv = window.nv;

var d3 = require('d3');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/chart.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  className: function() {
    return this.model.id === 'top-regions' ? 'col-xs-12' : 'col-xs-6';
  },

  initialize: function(options) {
    this.app = options.app;
    _(this).bindAll('getChart');
  },

  render: function() {
    this.$el.html(template({ chart: this.model, loading: true }));

    this.app.tryAfter(this.model.fetch(), function() {
      this.$el.html(template({ chart: this.model, loading: false }));
      nv.addGraph(_(function() { return this.app.tryTo(this.getChart, this); }).bind(this));
    }, this);

    return this;
  },

  getChart: function() {
    var values = _.map(this.model.get('values'), function(d) { return {x: d.name, y: d.amount}; });

    if (_.chain(values).pluck('x').uniq().value().length < values.length) {
      this.app.report('Data Error',
        ['The data for ' + this.model.get('name') + ' was inconsistent due to duplicate keys',
         'The chart will be shown, but it may have errors or other issues as a result.']);
    }

    var chart = nv.models.multiBarChart()
      .reduceXTicks(false);

    d3.select(this.el.querySelector('svg'))
      .datum([{
        key: 'Blah',
        values: values
      }])
      .transition().duration(1000)
      .call(chart);

    nv.utils.windowResize(chart.update);

    return chart;
  }

});
