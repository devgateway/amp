var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var ChartView = require('./chart');


module.exports = BackboneDash.View.extend({

  className: 'row',

  initialize: function(options) {
    this.app = options.app;
    this.chartViews = this.collection.map(function(chart) {
      return new ChartView({ model: chart, app: this.app });
    }, this);
    this.listenToOnce(this.app.filter, 'apply', this.applyFilter);
    this.listenTo(this.collection, 'change:big', this.injectBreaks);
  },

  render: function() {
    this.$el.html(_(this.chartViews).map(function(view) {
      return view.render().el;
    }));
    this.injectBreaks();
    return this;
  },

  injectBreaks: function(chartModel) {
    _(this.chartViews).reduce(function(breakAfter, thisView) {
      thisView.setClear(breakAfter);
      if (!breakAfter) {
        return thisView.model.get('big') ? false : true;
      } else {
        return false;
      }
    }, false);

    if (chartModel) {
      var chartView = _(this.chartViews).find(function(v) {
        return v.model === chartModel;
      });
      if (chartView) { chartView.render(); }
    }
  }

});
