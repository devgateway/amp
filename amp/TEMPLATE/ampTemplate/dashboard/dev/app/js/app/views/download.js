var _ = require('underscore');
var baby = require('babyparse');
var canvg = require('../../ugly/lib-load-hacks').canvg;
var util = require('../../ugly/util');

var fs = require('fs');
var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var getChart = require('../charts/chart');
var util = require('../../ugly/util');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/download.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  initialize: function(options) {
    this.app = options.app;
    this.dashChartOptions = _({}).extend(options.chartOptions, {
      height: 400,  // sync with css
      width: 700,
      trimLabels: false
    });
  },

  render: function() {
    this.$el.html(template());

    if (this.model.get('view') === 'table') {
      this.renderCSV(this.$('.preview-area .table-wrap').removeClass('hidden'));
    } else {
      this.renderChart(
        this.$('.preview-area .svg-wrap').removeClass('hidden'),
        this.$('.preview-area .canvas-wrap').removeClass('hidden'));
    }
    return this;
  },

  renderChart: function(svgContainer, canvasContainer) {
    if (_(this.app.browserIssues).findWhere({feature: 'canvas'})) {
      this.app.viewFail(this, 'Chart export requires a modern web browser');
    }
    var view = this.model.get('view'),
        data = this.model.get('processed'),
        canvas = document.createElement('canvas'),
        chartEl = getChart(view, data,
          _({}).extend(this.dashChartOptions, { height: this.dashChartOptions.height - 42 })).el;

    svgContainer.html(chartEl);

    this.prepareCanvas(canvas, this.dashChartOptions.height, this.dashChartOptions.width);

    this.chartToCanvas(chartEl, canvas, function() {
      svgContainer.hide();
      var img = new Image();
      img.src = canvas.toDataURL('image/png');
      canvasContainer.html(img);
      this.makeDownloadable(img.src, 'chart', '.png');
    });

  },

  prepareCanvas: function(canvas, h, w) {
    var ctx = canvas.getContext('2d'),
        moneyContext = this.model.get('currency'),
        adjType = this.model.get('adjtype'),
        key;

    if (adjType) {
      key = {
        ac: ['amp.dashboard:chart-radioui-commitments', 'Commitments'],
        ad: ['amp.dashboard:chart-radioui-disbursements', 'Disbursements']
      }[adjType];
      moneyContext = this.app.translator.translateSync(key[0], key[1]) + ' (' + moneyContext + ')';
    }

    // size the canvas
    canvas.setAttribute('width', w);
    canvas.setAttribute('height', h);

    // make the background opaque white
    ctx.beginPath();
    ctx.rect(0, 0, w, h);
    ctx.fillStyle = '#fff';
    ctx.fill();

    // Add the chart title
    ctx.fillStyle = '#163f66';
    ctx.font = 'bold 22px "Open Sans"';
    ctx.fillText(this.model.get('name').toUpperCase(), 10, 10 + 22);
    // what money are we talking about?
    ctx.fillStyle = '#333';
    ctx.textAlign = 'right';
    ctx.fillText(moneyContext, w - 10, 10 + 22);
    ctx.textAlign = 'left';  // reset it

    // reset font to something normal (nvd3 uses css ugh...)
    ctx.font = 'normal 12px "sans-serif"';
  },

  chartToCanvas: function(svg, canvas, cb) {
    var boundCB = _(cb).bind(this);
    window.setTimeout(function() {
      this.app.tryTo(function() {
        canvg(canvas, svg.parentNode.innerHTML, { // note: svg.outerHTML breaks IE
          offsetY: 42,
          ignoreDimensions: true,
          ignoreClear: true,
          ignoreMouse: true,
          renderCallback: boundCB
        });
      }, this);
    }.bind(this), 1500);  // we have to wait for stupid nvd3...
  },

  renderCSV: function(csvContainer) {
    var data = this.model.get('processed'),
        currency = this.model.get('currency'),
        adjtype = this.model.get('adjtype') || false,
        csvTransformed,
        headerRow,
        textContent,
        preview;

    // table of all the data
    csvTransformed = _(data)
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
    headerRow = [''];  // no header value for x-axis
    headerRow = headerRow.concat(_(data).pluck('key'));  // data headers
    headerRow.push('Currency');
    if (adjtype) { headerRow.push('Type'); }

    csvTransformed.unshift(headerRow);

    textContent = baby.unparse(csvTransformed);

    preview = document.createElement('textarea');
    preview.setAttribute('class', 'csv-preview');
    preview.value = textContent;
    csvContainer.html(preview);

    this.makeDownloadable(util.textAsDataURL(textContent), 'data', '.csv');
  },

  makeDownloadable: function(stuff, what, ext) {
    this.$('.download-chart')
      .removeClass('disabled')
      .attr('href', stuff)
      .attr('download', this.model.get('name') + ext)
      .find('.word').text('Download ' + what);
  }

});
