var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var ChartView = require('./chart');


module.exports = BackboneDash.View.extend({

  className: 'row',

  initialize: function(options) {
    this.app = options.app;
    this.chartViews = _(this.collection.map(function(chart) {
      return (new ChartView({ model: chart, app: this.app }));
    }, this));
    this.listenTo(this.collection, 'change:embiggen', this.injectBreaks);
  },

  render: function() {
    // TODO: get serialized state from the sharing stuff (don't wait for
    // filters to finish loading)
    if (this.app.filter.loaded.state() === 'pending') {
      this.$el.html('<h3 class="text-center">Loading...</h3>');
    }
    this.app.filter.loaded
      .always(_(function() {
        this.$el.html(this.chartViews.map(_(function(view) {
          return view.render().el;
        }).bind(this)));
        this.injectBreaks();
      }).bind(this));
    return this;
  },

  injectBreaks: function(chartModel) {
    this.chartViews.reduce(function(breakAfter, thisView) {
      thisView.setClear(breakAfter);
      if (!breakAfter) {
        return thisView.model.get('embiggen') ? false : true;
      } else {
        return false;
      }
    }, false);

    if (chartModel) {
      var chartView = this.chartViews.find(function(v) {
        return v.model === chartModel;
      });
      if (chartView) { chartView.render(); }
    }
  }

});
