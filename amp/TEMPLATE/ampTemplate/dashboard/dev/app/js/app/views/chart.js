var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var util = require('../../ugly/util');
var charts = require('./chart-types');
var Tops = require('../models/tops-chart');
var Predictability = require('../models/predictability-chart');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/chart.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  uiDefaults: function() {
    var defaults = {
      embiggen: false,
      view: this.model instanceof Tops ? 'bar'
          : this.model instanceof Predictability ? 'multibar'
          : null
    };
    if (!defaults.view) { console.error('unknown chart type for model', this.model); }
    return defaults;
  },

  events: {
    'change .dash-adj-type input': 'changeAdjType',
    'click .chart-view': 'changeChartView',
    'click .expand': 'embiggen'
  },

  initialize: function(options) {
    this.app = options.app;
    this.model.set(this.uiDefaults());
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
        this.renderNumbers();
      }).bind(this))
      .fail(_(function() {
        console.error('failed loading chart :(', arguments);
        this.app.report('Loading chart failed',
          ['There was an issue with your connection to the database, ' +
          'or the database may have crashed.']);
        this.$('svg').hide();
      }).bind(this));
  },

  renderNumbers: function() {
    if (this.model.get('total')) {
      this.$('.chart-total').html(util.formatKMB()(this.model.get('total')));
      this.$('.chart-currency').html(this.model.get('currency'));
    }
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
