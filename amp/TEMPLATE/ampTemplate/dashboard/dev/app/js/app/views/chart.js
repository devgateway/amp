var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var util = require('../../ugly/util');
var charts = require('./chart-types');
var Tops = require('../models/tops-chart');
var Predictability = require('../models/predictability-chart');
var FundingType = require('../models/ftype-chart');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/chart.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  uiDefaults: function() {
    var defaults = {
      big: this.model instanceof FundingType,  // funding type is big by default
      view: this.model instanceof Tops ? 'bar'
          : this.model instanceof Predictability ? 'multibar'
          : this.model instanceof FundingType ? 'multibar'
          : null
    };
    if (!defaults.view) { console.error('unknown chart type for model', this.model); }
    return defaults;
  },

  events: {
    'change .dash-adj-type input': 'changeAdjType',
    'click .chart-view': 'changeChartView',
    'click .expand': 'big',
    'click .retry': 'render'
  },

  initialize: function(options) {
    this.app = options.app;
    this.model.set(this.uiDefaults());
    // TODO: load any state we need
    this.chart = charts[this.model.get('view')]();  // bar, etc.
    this.listenTo(this.model, 'change:adjtype', this.updateData);
    this.listenTo(this.model, 'change:limit', this.updateData);
    this.listenTo(this.model, 'change:view', this.render);

    this.app.state.register(this, 'chart:' + this.model.url, {
      get: this._getState,
      set: _(this.model.set).bind(this.model),
      empty: null
    });
  },

  _getState: function() {
    return this.model.pick(
      'limit',
      'adjtype',
      'view',
      'big'
    );
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
    var message = this.$('.dash-chart-diagnostic');
    message.html('Loading...').fadeIn(100);
    var fetchOptions = {
      type: 'POST',
      data: JSON.stringify(this.app.filter.serialize())
    };
    this.model.fetch(fetchOptions)
      .done(_(function() {
        this.chart.draw(this.$('.dash-chart')[0], this.model);
        this.renderNumbers();
        message.stop().fadeOut(200);
      }).bind(this))
      .fail(_(function() {
        message.html('Failed to load data <small>' + arguments[2] +
          ' <button type="button" class="retry btn btn-warning btn-sm">' +
          '<span class="glyphicon glyphicon-refresh"></span> Retry</button></small>').show();
        console.error('failed loading chart :(', arguments);
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
    this.chart = charts[view]();
    this.model.set('view', view);
  },

  big: function() {
    // toggle big/small charts on large screens
    this.model.set('big', !this.model.get('big'));
  },

  setClear: function(shouldBreak) {
    // layout hints, should only be called by ./charts.js
    this.$el[shouldBreak ? 'addClass' : 'removeClass']('clearfix');
  }

});
