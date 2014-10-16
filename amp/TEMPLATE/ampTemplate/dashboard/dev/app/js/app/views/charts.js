var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var ChartView = require('./chart');
var chartsLoading = _.template(fs.readFileSync(
  __dirname + '/../templates/charts-loading.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  className: 'row',

  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    this.$el.html(chartsLoading());

    this.app.tryAfter(this.collection.fetch(), function() {
      this.$el.html(this.collection.map(function(chart) {
        return (new ChartView({ model: chart, app: this.app })).render().el;
      }));
    }, this);

    return this;
  }

});
