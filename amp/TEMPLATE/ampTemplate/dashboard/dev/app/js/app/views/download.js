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

var previousXLimit = null;
var previousYLimit = null;

module.exports = BackboneDash.View.extend({

  //TODO: This is wrong because different countries have other measures (ie: ssc).
  adjTypeTranslation : {"Actual Commitments":"amp.dashboard:ftype-actual-commitment",
			"Actual Disbursements":"amp.dashboard:ftype-actual-disbursement",
				"Actual Expenditures":"amp.dashboard:ftype-actual-expenditure",
				"Planned Commitments": "amp.dashboard:ftype-planned-commitment",
				"Planned Disbursements": "amp.dashboard:ftype-planned-disbursement",
				"Planned Expenditures": "amp.dashboard:ftype-planned-expenditures"
			    },
  initialize: function(options) {
	var self = this;
    this.app = options.app;
    var valuesLength = this.model.get('values') ? this.model.get('values').length : 0;
    var height = util.calculateChartHeight(valuesLength, true);
    this.dashChartOptions = _({}).extend(options.chartOptions, {
      height: height, //450,  // sync with css!!!
      width: $('.container').width(),	// sync with css!!!
      trimLabels: false,
      nvControls: false      
    });
    
    // Heatmaps export need to show all data at once so we force the x/y limits and re-render the chart.
    if (this.model.get('chartType') === 'fragmentation') {
	    previousXLimit = this.model.get('xLimit');
	    previousYLimit = this.model.get('yLimit');
	    this.model.set('yLimit', 10000);
	    this.model.set('xLimit', 10000);
	    this.model.set('showFullLegends', true);
	    var chart = _.find(this.app.view.charts.chartViews, function(item) {return item.model.get('name') === self.model.get('name')});
	    chart.render();
    }
  },

  render: function() {
	var self = this;
    this.$el.html(template());

    if (this.model.get('view') === 'table') {
      this.renderCSV(this.$('.preview-area .table-wrap').removeClass('hidden'));
    } else {
    	// Here we will define an interval that will check periodically if the bootstrap modal is fully rendered.
    	// In that moment the interval is finished and the chart is rendered.
    	var self = this;
    	var rendered = false; // This flag is used to avoid triggering the render process twice in case the browser mess up the interval.
    	var chart = _.find(self.app.view.charts.chartViews, function(item) {return item.model.get('name') === self.model.get('name')});
    	var interval = window.setInterval(function() {
    		$.when(chart.showChartPromise.then(function() {
    			if ($('.dash-download-modal').closest('.in').length > 0) {
        			window.clearInterval(interval);
        			nv.tooltip.cleanup();
        			if (rendered === false) {
        				rendered = true;
        				    				
        			    if (self.model.get('chartType') === 'fragmentation') {
        			    	// We add an event for heatmaps to re-draw the original chart.
        			    	$('.dash-download-modal').closest('.in').on('hide.bs.modal', function() {
        			    		self.model.set('yLimit', previousYLimit);
        			    	    self.model.set('xLimit', previousXLimit);
        			    	    self.model.set('showFullLegends', false);
        			    	    chart.render();
        			    	});
        			    }
        			    
        				self.renderChart(self.$('.preview-area .svg-wrap').removeClass('hidden'),
        						self.$('.preview-area .canvas-wrap'), chart);
        			}
        		}
    		}, this));    		
    	}, 100);
    }
    return this;
  },

  renderChart: function(svgContainer, canvasContainer, chart) {
	var self = this;
    if (_(this.app.browserIssues).findWhere({feature: 'canvas'})) {
      this.app.viewFail(this, 'Chart export requires a modern web browser');
    }
    
    if (self.model.get('chartType') === 'fragmentation') {
    	var svg = $($($(chart)[0].el).find("svg"))[0].getBBox();
	    this.dashChartOptions.height = svg.height + 100;
	    this.dashChartOptions.width = svg.width + 80;
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
      $(canvasContainer).removeClass('hidden');
      $('.modal-preview-area').remove();
      this.makeDownloadable(img.src, 'chart', '.png');
    });
    
    // Scale the modal correctly for heatmaps.
    if (self.model.get('chartType') === 'fragmentation') {
    	var modal = $('.dash-download-modal').closest('.in').find('.dash-download-modal');
    	if ($(svgContainer).width() > $(modal).width()) {
    		$(modal).find('.preview-area').css('max-width','90%');
        	$(modal).closest('.in').find('.preview-area').css('overflow','auto');
    	}
    	if ($(svgContainer).height() > $(window).height()) {
    		$(modal).find('.preview-area').css('max-height', ($(window).height() - 270) + 'px');
        	$(modal).closest('.in').find('.preview-area').css('overflow','auto');
    	}
    }
  },

  prepareCanvas: function(canvas, h, w) {
	var self = this;
	var currency = _.find(app.settings.get('1').get('options'), function(item) {return item.id === self.model.get('currency')});
    var currencyName = currency !== undefined ? currency.value : '';
    var ctx = canvas.getContext('2d'),
    	moneyContext = (this.model.get('sumarizedTotal') !== undefined ? ': ' + util.translateLanguage(this.model.get('sumarizedTotal')) + ' ': ' ') + currencyName,
        adjType = this.model.get('adjtype');    
    if (adjType) {
    	var key = this.adjTypeTranslation [adjType];
        var trnAdjType = this.app.translator.translateSync(key, adjType);
        moneyContext = trnAdjType + moneyContext;
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
    ctx.fillText(this.model.get('title').toUpperCase(), 10, 10 + 22);
    // what money are we talking about?
    ctx.fillStyle = '#333';
    if (self.model.get('chartType') === 'fragmentation') {
    	ctx.font = 'normal 14px "Open Sans"';
    	ctx.textAlign = 'left';
    	ctx.fillText(moneyContext, 10, 50);	    
    } else {    
    	ctx.textAlign = 'right';
	    ctx.fillText(moneyContext, w - 10, 10 + 22);
	    ctx.textAlign = 'left';  // reset it
    }    
    // reset font to something normal (nvd3 uses css ugh...)
    ctx.font = 'normal 12px "sans-serif"';
    
    $('.modal.in .modal-dialog').width(w + 60);
  },

  chartToCanvas: function(svg, canvas, cb) {
	var self = this;
	
	if (this.model.get('chartType') === 'fragmentation') {
		var css = "rect.bordered {stroke: #E6E6E6;stroke-width: 2px;} text.mono {font-size: 9pt;font-family: Arial;fill: #000;}";
	    var s = document.createElement('style');
	    s.setAttribute('type', 'text/css');
	    s.innerHTML = "<![CDATA[\n" + css + "\n]]>";
	    //var defs = document.createElement('defs');
	    //defs.appendChild(s);
	    svg.getElementsByTagName("defs")[0].appendChild(s);
	    //svg.insertBefore(defs, canvas.firstChild);    
	    //svg.appendChild(defs);
	}
	
    var boundCB = _(cb).bind(this);
    window.setTimeout(function() {
      this.app.tryTo(function() {
        canvg(canvas, svg.parentNode.innerHTML, { // note: svg.outerHTML breaks IE
          offsetY: ((self.model.get('chartType') !== 'fragmentation') ? 42 : 65),
          ignoreDimensions: true,
          ignoreClear: true,
          ignoreMouse: true,
          renderCallback: boundCB
        });
      }, this);
    }.bind(this), 1500);  // we have to wait for stupid nvd3...
  },

  renderCSV: function(csvContainer) {
	var self = this;
	var currencyName = _.find(app.settings.get('1').get('options'), function(item) {return item.id === self.model.get('currency')}).value;
    var data = this.model.get('processed'),
        currency = currencyName,
        adjtype = this.model.get('adjtype') || false,
        csvTransformed,
        headerRow,
        textContent,
        preview;

    var self = this;
    var keys = _(data).pluck('key');
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
        if (adjtype) {
        	var key = self.adjTypeTranslation [adjtype];
            var trnAdjType = this.app.translator.translateSync(key, adjtype);
            row.push(trnAdjType);
        }
        return row;
      })
      .value();

    // prepend a header row
    headerRow = [];
    var amountTrn = this.app.translator.translateSync('amp.dashboard:download-amount', 'Amount');
    var currencyTrn = this.app.translator.translateSync('amp.dashboard:currency', 'Currency');
    var typeTrn = this.app.translator.translateSync('amp.dashboard:type', 'Type');
    var yearTrn = this.app.translator.translateSync('amp.dashboard:year', 'Year');

	if (this.model.url.indexOf('/tops') > -1) {
	    headerRow.push(this.model.get('title'));
	    headerRow.push(amountTrn);
	    headerRow.push(currencyTrn);
	    headerRow.push(typeTrn);
	} else if (this.model.url.indexOf('/aid-predictability') > -1) {
	    headerRow.push(yearTrn);
	    _.each(keys, function(item) {
	    	headerRow.push(item);
	    });
	    headerRow.push(currencyTrn);
	} else if (this.model.url.indexOf('/ftype') > -1) {
		headerRow.push(yearTrn);
	    _.each(keys, function(item) {
	    	headerRow.push(item);
	    });
	    headerRow.push(currencyTrn);
	    headerRow.push(typeTrn);
	}

    csvTransformed.unshift(headerRow);
    /* Add sep=, for automatic Excel support at the very top of the file works but breaks BOM unicode.
     * Let us use tab-delimited instead.
     *  This website shows a csv with Tab-delimited, utf16le with a BOM has best Excel support (via StackOverflow):
     *  http://wiki.scn.sap.com/wiki/display/ABAP/CSV+tests+of+encoding+and+column+separator
     */

    textContent = baby.unparse(csvTransformed, {
      delimiter: ';',
      encoding: 'utf-16',
    	quotes: true
    });

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
	var messageKey = ['amp.dashboard:chart-', this.model.get('name').replace(/ /g, '')].join('')
	var translatedName = app.translator.translateSync(messageKey, this.model.get('name'));
    var fileName = translatedName + ext,
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

    // AMP-19813
    if (ext.indexOf('csv') !== -1) {
    	$('.modal-preview-area').remove();
    }
  }

});
