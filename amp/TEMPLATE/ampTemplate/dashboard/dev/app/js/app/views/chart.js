var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var baby = require('babyparse');
var canvg = require('../../ugly/lib-load-hacks').canvg;
var BackboneDash = require('../backbone-dash');
var util = require('../../ugly/util');


module.exports = BackboneDash.View.extend({


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

  resetLimit: function() {
    this.model.set('limit', this.model.defaults.limit);
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
    var data = this.model.get('processed'),
        currency = this.model.get('currency'),
        adjtype = this.model.get('adjtype') || null;
    // table of all the data
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
      .map(function(row) {
        row.push(currency);
        if (adjtype) { row.push(adjtype); }
        return row;
      })
      .value();
    // prepend a header row
    var headerRow = [''];  // no header value for x-axis
    headerRow = headerRow.concat(_(data).pluck('key'));  // data headers
    headerRow.push('Currency');
    if (adjtype) { headerRow.push('Type'); }

    csvTransformed.unshift(headerRow);

    e.currentTarget.setAttribute('href', util.textAsDataURL(baby.unparse(csvTransformed)));
    e.currentTarget.setAttribute('download',
      this.model.get('name') + '.csv');
  },

  drawChartForDownload: function(canvas, options) {
    var ctx = canvas.getContext('2d'),
        w = canvas.getAttribute('width'),
        h = canvas.getAttribute('height');

    var moneyContext = options.currency;
    if (options.adjtype) {
      var key = options.adjtype === 'ac' ?
        ['amp.dashboard:chart-radioui-commitments', 'Commitments'] :
        ['amp.dashboard:chart-radioui-disbursements', 'Disbursements'];
      moneyContext = this.app.translator.translateSync(key[0], key[1]) +
        ' (' + moneyContext + ')';
    }

    // make the background opaque white
    ctx.beginPath();
    ctx.rect(0, 0, w, h);
    ctx.fillStyle = '#fff';
    ctx.fill();

    // Add the chart title
    ctx.fillStyle = '#163f66';
    ctx.font = 'bold 22px "Open Sans"';
    ctx.fillText(options.title.toUpperCase(), 10, 10 + 22);
    // what money are we talking about?
    ctx.fillStyle = '#333';
    ctx.textAlign = 'right';
    ctx.fillText(moneyContext, w - 10, 10 + 22);
    ctx.textAlign = 'left';  // reset it

    // reset font to something normal (nvd3 uses css ugh...)
    ctx.font = 'normal 13px "sans-serif"';
  },

  downloadChart: function(e) {
    this.app.modal('Chart Download', 'suuuuuuup');
    var wrapper = document.createElement('div'),
        svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
    document.body.appendChild(wrapper);
    wrapper.appendChild(svg);
    wrapper.setAttribute('style', 'height: 640px; width: 1200px;');
    // svg.setAttribute('style', 'height: 100%; width: 100%;');
    svg.classList.add('dash-chart');

    this.chart(wrapper, this.model, true);


    var canvas = document.createElement('canvas');
    if (!canvas.getContext) {
      this.app.report('Unsupported feature',
        ['Chart export is not supported on this browser.',
         'Please upgrade to a modern browser to export charts.']);
      return;
    }
    canvas.setAttribute('width', svg.offsetWidth);
    canvas.setAttribute('height', svg.offsetHeight + 42);
    console.log(canvas);

    this.drawChartForDownload(canvas, {
      title: this.model.get('name'),
      currency: this.model.get('currency'),
      adjtype: this.model.get('adjtype')
    });

    setTimeout(function() {
      canvg(canvas, svg.outerHTML, {
        offsetY: 42,
        ignoreClear: true,  // don't erase the title/bg!
        // log: true,  // debug info
        ignoreMouse: true,
        ignoreAnimation: true,
        ignoreDimensions: true
      });
      document.body.appendChild(canvas);
    }, 2000);
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
