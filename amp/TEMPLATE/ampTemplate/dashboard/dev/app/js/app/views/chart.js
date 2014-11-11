var fs = require('fs');
var _ = require('underscore');
var canvg = require('../../ugly/lib-load-hacks').canvg;
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

  stateKeys: ['limit', 'adjtype', 'view', 'big'],

  events: {
    'change .dash-adj-type input': 'changeAdjType',
    'click .chart-view': 'changeChartView',
    'click .download': 'download',
    'click .expand': 'big',
    'click .retry': 'render'
  },

  initialize: function(options) {
    this.app = options.app;
    this.model.set(this.uiDefaults());
    this.rendered = false;
    this.listenTo(this.app.filter, 'apply', this.updateData);
    this.listenTo(this.model, 'change:adjtype', this.updateData);
    this.listenTo(this.model, 'change:limit', this.updateData);
    this.listenTo(this.model, 'change:view', this.render);

    this.app.state.register(this, 'chart:' + this.model.url, {
      get: this._getState,
      set: this._setState,
      empty: null
    });

    this.chart = charts[this.model.get('view')]();  // bar, etc.
  },

  _getState: function() {
    var states =  _(this.stateKeys).map(function(k) {
      var s = {},
          uiDefaults = this.uiDefaults(),
          modelState = this.model.get(k);
      if (_(uiDefaults).has(k) && uiDefaults[k] !== modelState ||
          _(this.model.defaults).has(k) && this.model.defaults[k] !== modelState) {
        s[k] = modelState;
      }
      return s;
    }, this);

    return _.extend.apply(null, states);
  },

  _setState: function(states) {
    this.model.set(_({}).extend(
      _(this.uiDefaults()).pick(this.stateKeys),
      _(this.model.defaults).pick(this.stateKeys),
      states
    ));
  },

  render: function() {
    this.rendered = true;
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
    if (!this.rendered) {
      return;
    }
    var message = this.$('.dash-chart-diagnostic');
    message.html('Loading...').fadeIn(100);
    var fetchOptions = {
      type: 'POST',
      data: JSON.stringify(this.app.filter.serialize())
    };
    this.model.fetch(fetchOptions)
      .done(_(function() {
        this.chart(this.el.querySelector('.dash-chart-wrap'), this.model);
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

  drawChartTitle: function(canvas, title) {
    var ctx = canvas.getContext('2d');
    ctx.fillStyle = '#163f66';
    ctx.font = 'bold 22px "Open Sans"';
    ctx.fillText(title.toUpperCase(), 10, 10+22);
  },

  download: function(e) {
    var svg = this.el.querySelector('svg.dash-chart');
    if (!svg) {
      this.app.report('Chart export was unsuccessful',
        ['Could not find chart svg to render.']);
      return;
    }

    var canvas = document.createElement('canvas');
    if (!canvas.getContext) {
      this.app.report('Unsupported feature',
        ['Chart export is not supported on this browser.']);
      return;
    }
    canvas.setAttribute('width', this.$(svg).width());
    canvas.setAttribute('height', this.$(svg).height() + 42);

    canvg(canvas, svg.outerHTML, {
      offsetY: 42,
      // log: true,  // debug info
      ignoreMouse: true,
      ignoreAnimation: true
    });
    // canvg renders synchronously, despite having a callback option

    this.drawChartTitle(canvas, this.model.get('name'));

    e.currentTarget.href = canvas.toDataURL('image/png');
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
