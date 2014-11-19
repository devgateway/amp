var fs = require('fs');
var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var baby = require('babyparse');
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

  events: {
    'change .dash-adj-type input': 'changeAdjType',
    'click .reset': 'resetLimit',
    'click .chart-view': 'changeChartView',
    'click .download': 'download',
    'click .expand': 'big',
    'click .retry': 'render'
  },

  initialize: function(options) {
    this.app = options.app;
    this.model.set(this.uiDefaults());
    this.rendered = false;
    this._stateWait = new Deferred();

    if (this.app.savedDashes.length) {
      // a bit sketch....
      this.app.state.loadPromise.always(this._stateWait.resolve);
    } else {
      this._stateWait.resolve();
    }

    this.listenTo(this.app.filter, 'apply', this.updateData);
    this.listenTo(this.model, 'change:adjtype', this.render);
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
    this.rendered = true;
    this.chart = charts[this.model.get('view')]();  // bar, etc.
    var renderOptions = {
      model: this.model,
      chart: this.chartEl,
      util: util
    };
    this.$el.html(template(renderOptions));
    if (this._stateWait.state() !== 'pending') {
      this.updateData();
    }
    return this;
  },

  updateData: function() {
    if (!this.rendered) { return; }  // cop-out on early filters apply event

    var message = this.$('.dash-chart-diagnostic');

    if (this._stateWait.state() === 'pending') {
      // don't do anything until we have state
      message.html('Loading saved settings...').fadeIn(100);
      return;
    }

    message.html('Loading...').fadeIn(100);
    var fetchOptions = {
      type: 'POST',
      data: JSON.stringify(this.app.filter.serialize())
    };
    var countValues = function(processed) {
      return _(processed)
        .chain()
        .pluck('values')
        .reduce(function(prev, values) {
          return prev + values.length;
        }, 0)
        .value();
    };
    this.model.fetch(fetchOptions)
      .done(_(function() {
        if (countValues(this.model.get('processed')) ===  0) {
          message.html('No Data Available');
          this.$('svg').empty();
        } else {
          this.chart(this.el.querySelector('.dash-chart-wrap'), this.model);
          this.renderNumbers();
          var limit = this.model.get('limit');
          if (limit) {
            this.$('.reset')[limit === this.model.defaults.limit ? 'hide' : 'show']();
          }
          message.stop().fadeOut(200);
        }
      }).bind(this))
      .fail(_(function() {
        message.html('Failed to load data <small>' + arguments[2] +
          ' <button type="button" class="retry btn btn-warning btn-sm">' +
          '<span class="glyphicon glyphicon-refresh"></span> Retry</button></small>').show();
        console.error('failed loading chart :(', arguments);
        this.$('svg').hide();
      }).bind(this));
  },

  resetLimit: function() {
    this.model.set('limit', this.model.defaults.limit);
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
  },

  download: function(e) {
    if (this.model.get('view') === 'table') {
      this.downloadCSV(e);
    } else {
      this.downloadChart(e);
    }
  },

  downloadCSV: function(e) {
    var data = this.model.get('processed');
    var csvTransformed = _(data)
      .chain()
      .pluck('values')
      .transpose()
      .map(function(row) {
        return _(row).reduce(function(csvRow, cell) {
          csvRow.push(cell.y);
          return csvRow;
        }, [row[0].x]);
      })
      .value();
    if (data.length > 1) {  // multiseries; add a header
      csvTransformed.unshift(_(data).pluck('key'));
      csvTransformed[0].unshift('x');  // header for x axis values.
    }

    var downloadable = 'data:text/plain;charset=utf-8,' +
      encodeURIComponent(baby.unparse(csvTransformed));

    e.currentTarget.setAttribute('href', downloadable);
    e.currentTarget.setAttribute('download',
      this.model.get('name') + '.csv');
  },

  drawChartForDownload: function(canvas, title) {
    var ctx = canvas.getContext('2d');

    // make the background opaque white
    ctx.beginPath();
    ctx.rect(0, 0, canvas.getAttribute('width'), canvas.getAttribute('height'));
    ctx.fillStyle = '#fff';
    ctx.fill();

    // Add the chart title
    ctx.fillStyle = '#163f66';
    ctx.font = 'bold 22px "Open Sans"';
    ctx.fillText(title.toUpperCase(), 10, 10 + 22);
  },

  downloadChart: function(e) {
    var svg = this.el.querySelector('svg.dash-chart');
    if (!svg) {
      this.app.report('Chart export was unsuccessful',
        ['Could not find chart svg to render.']);
      return;
    }

    var canvas = document.createElement('canvas');
    document.body.appendChild(canvas);
    if (!canvas.getContext) {
      this.app.report('Unsupported feature',
        ['Chart export is not supported on this browser.']);
      return;
    }
    canvas.setAttribute('width', this.$(svg).width());
    canvas.setAttribute('height', this.$(svg).height() + 42);

    this.drawChartForDownload(canvas, this.model.get('name'));

    canvg(canvas, svg.outerHTML, {
      offsetY: 42,
      ignoreClear: true,  // don't erase the title/bg!
      // log: true,  // debug info
      ignoreMouse: true,
      ignoreAnimation: true
    });
    // canvg renders synchronously, despite having a callback option

    e.currentTarget.setAttribute('href', canvas.toDataURL('image/png'));
    e.currentTarget.setAttribute('download',
      this.model.get('name') + '.png');
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
