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
    this.$el.html(template(this.model.toJSON()));
    nv.addGraph(this.getChart);
    return this;
  },

  getChart: function() {
    var chart = nv.models.multiBarChart()
      .reduceXTicks(false);

    d3.select(this.el.querySelector('svg'))
      .datum([{
        key: 'Blah',
        values: [
          {x: 0, y: 7},
          {x: 1, y: 5},
          {x: 2, y: 5},
          {x: 3, y: 3},
          {x: 4, y: 2}
        ]
      }])
      .transition().duration(1000)
      .call(chart);

    nv.utils.windowResize(chart.update);

    return chart;
  }

});
