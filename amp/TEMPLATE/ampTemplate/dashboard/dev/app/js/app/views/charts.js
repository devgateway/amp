var BackboneDash = require('../backbone-dash');
var ChartView = require('./chart.js');


module.exports = BackboneDash.View.extend({

  className: 'row',

  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    this.$el.html(this.collection.map(function(chart) {
      return (new ChartView({ model: chart }, { app: this.app })).render().el;
    }));
    return this;
  }

});
