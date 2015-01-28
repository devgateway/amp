var _ = require('underscore');
var baby = require('babyparse');
var canvg = require('../../ugly/lib-load-hacks').canvg;
var util = require('../../ugly/util');

var fs = require('fs');
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
        adjType = this.model.get('adjtype');

    if (adjType) {
      var adjSettings = this.app.settings.get('0');  // id for Funding Type
      if (!adjSettings) { this.app.report('Could not find Funding Type settings'); }
      var adjName = _(adjSettings.get('options')).find(function(s) {
        return s.id === adjType;
      }).name;

      // var localAdj = this.app.translator.translateSync()
      // TODO: localize adjtype? is that necessary?
      moneyContext = adjName + ' (' + moneyContext + ')';
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
    var localName = this.app.translator.translateSync('amp.dashboard:chart-' +
      this.model.get('name').replace(/ /g, ''), this.model.get('name'));
    ctx.fillText(localName.toUpperCase(), 10, 10 + 22);
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
        row.push(currency || '');
        if (adjtype) { row.push(adjtype); }
        return row;
      })
      .value();

    // prepend a header row
    headerRow = [''];  // no header value for x-axis
    headerRow = headerRow.concat(_(data).map(function(col) {
      return col.key || '';  // data headers
    }));
    headerRow.push('Currency');
    if (adjtype) { headerRow.push('Type'); }

    csvTransformed.unshift(headerRow);

    textContent = baby.unparse(csvTransformed);

    preview = document.createElement('textarea');
    preview.setAttribute('class', 'csv-preview');
    preview.value = textContent;
    csvContainer.html(preview);

    if (!this.app.hasIssue('download')) {
      textContent = util.textAsDataURL(textContent);
    }

    window.setTimeout(_(function() {  // stupid bootstrap modals...
      // this setTimeout is needed for the flash fallback :(
      this.makeDownloadable(textContent, 'data', '.csv');
    }).bind(this), 100);
  },

  makeDownloadable: function(stuff, what, ext) {
    var fileName = this.model.get('name') + ext,
        dlButton = this.$('.download-chart').removeClass('disabled');
    dlButton.find('.word').text('Download ' + what).attr('data-i18n', 'amp.dashboard:download-download-' + what);
    app.translator.translateDOM(dlButton);

    if (this.app.hasIssue('download')) {
      if (this.app.hasIssue('flash')) {
        this.app.report('Your browser is missing features to initiate the download', [
          'You might be able to save this chart manually by right-clicking the ' +
          'preview and selecting "Save Picture As...']);
      } else {
        // bad browser, but has flash! fallback to downloadify
        this.app.tryTo(function() {
          window.Downloadify.create(dlButton[0], {
            swf: '/TEMPLATE/ampTemplate/commonMedia/downloadify.swf',
            downloadImage: '/TEMPLATE/ampTemplate/commonMedia/download-button-states.png?rev=4',
            transparent: true,
            width: 176,
            height: 34,
            filename: fileName,
            data: stuff
              .replace('data:text/plain;base64,//', '')
              .replace('data:image/png;base64,', ''),
            dataType: ext === '.csv' ? 'string' : 'base64',
            append: true,
            onError: function() {
              this.app.report('Could not download the file.');
            }
          });
        }, this);
      }
    } else {
      this.$('.download-chart')
        .attr('href', stuff)
        .attr('download', fileName);
    }
  }

});
