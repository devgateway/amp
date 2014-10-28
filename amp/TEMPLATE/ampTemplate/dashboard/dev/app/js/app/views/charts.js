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
    this.$el.html(this.chartViews.map(_(function(view) {
      return view.render().el;
    }).bind(this)));

    this.injectBreaks();
    return this;
  },

  injectBreaks: function() {
    this.chartViews.reduce(function(breakAfter, thisView) {
      thisView.setClear(breakAfter);
      if (!breakAfter) {
        return thisView.model.get('embiggen') ? false : true;
      } else {
        return false;
      }
    }, false);
  }

});
