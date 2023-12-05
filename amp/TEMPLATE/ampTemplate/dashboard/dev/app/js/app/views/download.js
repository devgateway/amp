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

  initialize: function(options) {
	var self = this;
    this.app = options.app;
      if (app && app.generalSettings && app.generalSettings.attributes && app.generalSettings.attributes['rtl-direction']) {
          this.isRtl = true;
      } else {
          this.isRtl = false;
      }
    var valuesLength = this.model.get('values') ? this.model.get('values').length : 0;
    var sourceHeight = this.model.get('source') ? 22 : 0;
    var height = util.calculateChartHeight(valuesLength, true) + sourceHeight;
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
	    this.model.set('yLimit', -1);
	    this.model.set('xLimit', -1);
	    this.model.set('showFullLegends', true);
	    var chart = _.find(this.app.view.charts.chartViews, function(item) {return item.model.get('name') === self.model.get('name')});
	    chart.render();
    }
  },

  render: function() {
	var self = this;
    this.$el.html(template());   
    this.chart = _.find(self.app.view.charts.chartViews, function(item) {return item.model.get('name') === self.model.get('name')});
    
	// Here we will define an interval that will check periodically if the bootstrap modal is fully rendered.
	// In that moment the interval is finished and the chart is rendered.
	var rendered = false; // This flag is used to avoid triggering the render process twice in case the browser mess up the interval.	    	
	var interval = window.setInterval(function() {
		if ($('.dash-download-modal').closest('.in').length > 0) {
			window.clearInterval(interval);
			// Wait for the chart in the dashboard page to be fully rendered, this has impact only on heatmap charts, on the rest is transparent.
			$.when(self.chart.renderedPromise, self.chart.showChartPromise).done(function() {
	    		if (self.model.get('chartType') === 'fragmentation') {
	    			// We add an event for heatmaps to re-draw the original chart.
	    		    $('.dash-download-modal').closest('.in').on('hide.bs.modal', function() {
	    		    	self.model.set('yLimit', previousYLimit);
	    		    	self.model.set('xLimit', previousXLimit);
	    		    	self.model.set('showFullLegends', false);
						self.chart.render();
	    		    });
	    		}
			    if (self.model.get('view') === 'table') {
			    	self.renderCSV(self.$('.preview-area .table-wrap').removeClass('hidden'));
			    } else {
			        nv.tooltip.cleanup();
			        if (rendered === false) {
			        	rendered = true;
                        if (self.model.get('chartType') === 'fragmentation') {
                            var svg = $($($(self.chart)[0].el).find("svg"))[0].getBBox();
                            self.dashChartOptions.height = svg.height + 100;
                            self.dashChartOptions.width = svg.width + 80;
                        }

			        	self.renderChart(self.$('.preview-area .svg-wrap').removeClass('hidden'),
			        		self.$('.preview-area .canvas-wrap'), self.chart);
			        }
			    }
			});
		}
    }, 100);
    return this;
  },

  renderChart: function(svgContainer, canvasContainer, chart) {
	var self = this;
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
    svgContainer.hide();

    this.chartToCanvas(chartEl, canvas, function() {

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
        var currency = app.settingsWidget.definitions.findCurrencyById(self.model.get('currency'));
        var currencyName = currency !== undefined ? currency.value : '';

        var moneyContext ='';
        var  adjType = this.model.get('adjtype');
        var trnAdjType ='';
        if (adjType) {
            trnAdjType = this.chart.$el.find('.ftype-options option:selected').text();
        }
        moneyContext = (this.model.get('sumarizedTotal') !== undefined ? ': '
            + util.translateLanguage(this.model.get('sumarizedTotal')) + ' ': ' ') + currencyName;
        moneyContext = trnAdjType + moneyContext;

        // size the canvas
        canvas.setAttribute('width', w);
        canvas.setAttribute('height', h);

        var ctx = canvas.getContext('2d');
        // make the background opaque white
        ctx.direction = self.isRtl ? 'rtl' : 'ltr';
        ctx.textAlign='start';
        ctx.beginPath();
        ctx.rect(0, 0, w, h);
        ctx.fillStyle = '#fff';
        ctx.fill();

        // Add the chart title
        ctx.fillStyle = '#163f66';
        ctx.font = 'bold 22px "Open Sans"';
        var strTitle = this.model.get('title');
        var titleX = 10;
        var trnAdustTypeX = 10;
        var moneyContextX = w-10;
        if (self.isRtl) {
            //for title
            titleX = w - 10;
            //for adjustment type
            trnAdustTypeX = w - 10;
            //for currency
            moneyContextX = 10;
        }
        ctx.fillText(strTitle.toUpperCase(), titleX, 10 + 22);

        // Add source
        var source = this.model.get('source');
        if (source) {
            ctx.fillStyle = '#000';
            ctx.font = 'normal 14px "Open Sans"';
            ctx.fillText(source, titleX, h - 10);
        }

        // what money are we talking about?
        ctx.fillStyle = '#333';
        if (self.model.get('chartType') === 'fragmentation') {
            ctx.font = 'normal 14px "Open Sans"';
            ctx.fillText(trnAdjType, trnAdustTypeX, 50);
        } else {
            ctx.textAlign = 'end';
            ctx.fillText(moneyContext, moneyContextX, 10 + 22);
            ctx.textAlign = 'start';
        }
        // reset font to something normal (nvd3 uses css ugh...)
        ctx.font = 'normal 12px "sans-serif"';



        $('.modal.in .modal-dialog').width(w + 60);
    },

  chartToCanvas: function(svg, canvas, cb) {
	var self = this;
	
	if (this.model.get('chartType') === 'fragmentation') {
		// This is what applies the necessary styles to the chart´s SVG.
		var css = "rect.bordered {stroke: #E6E6E6;stroke-width: 2px;} text.mono {font-size: 9pt;font-family: Arial;fill: #000;}";
	    var s = document.createElement('style');
	    s.setAttribute('type', 'text/css');
	    s.innerHTML = "<![CDATA[\n" + css + "\n]]>";
	    svg.getElementsByTagName("defs")[0].appendChild(s);
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
    }.bind(this), 1500);  // we have to wait for nvd3 to load
  },

  renderCSV: function(csvContainer) {
      var self = this;
      var currencyName = app.settingsWidget.definitions.findCurrencyById(self.model.get('currency')).value;
      var data = this.model.get('processed'),
          currency = currencyName,
          adjtype = this.model.get('adjtype') || false,
          csvTransformed,
          headerRow,
          textContent,
          preview;

      var self = this;
      var keys = _(data).pluck('key');

      if (self.model.get('chartType') !== 'fragmentation') {
          // table of all the data
          csvTransformed = _(data)
              .chain()
              .pluck('values')
              .transpose()
              .map(function (row) {
                  return _(row).reduce(function (csvRow, cell) {
                      csvRow.push(cell.y);
                      return csvRow;
                  }, [row[0].x]);
              })
              .map(function (row) {
                  var trnAdjType = '';
                  row.push(currency || '');
                  if (adjtype) {
                      trnAdjType = self.chart.$el.find('.ftype-options option:selected').text();
                      row.push(trnAdjType);
                  }
                  return row;
              })
              .value();
      } else {
          csvTransformed = _.map(self.model.get("matrix"), function (itemY, i) {
              return _.map(itemY, function (itemX, j) {
                  return [self.model.get("yDataSet")[i],
                      self.model.get("xDataSet")[j],
                      self.model.get("matrix")[i][j] ? self.model.get("matrix")[i][j].dv : '',
                      self.model.get("matrix")[i][j] ? self.model.get("matrix")[i][j].p : ''
                  ]
              })
          });
          csvTransformed = [].concat.apply([], csvTransformed);
          csvTransformed = _.each(csvTransformed, function (item) {
              item.push(currency);
              if (adjtype) {
                  var trnAdjType = self.chart.$el.find('.ftype-options option:selected').text();
                  item.push(trnAdjType);
              }
          });
      }

      // prepend a header row
      headerRow = [];
      var amountTrn = this.app.translator.translateSync('amp.dashboard:download-amount', 'Amount');
      var currencyTrn = this.app.translator.translateSync('amp.dashboard:currency', 'Currency');
      var percentageTrn = this.app.translator.translateSync('amp.dashboard:percentage', 'Percentage');
      var typeTrn = this.app.translator.translateSync('amp.dashboard:type', 'Type');
      var yearTrn = this.app.translator.translateSync('amp.dashboard:year', 'Year');

      if (this.model.url.indexOf('/tops') > -1) {
          if (self.isRtl) {
              headerRow.push(typeTrn);
              headerRow.push(currencyTrn);
              headerRow.push(amountTrn);
              headerRow.push(this.model.get('title'));
          } else {
              headerRow.push(this.model.get('title'));
              headerRow.push(amountTrn);
              headerRow.push(currencyTrn);
              headerRow.push(typeTrn);
          }

      } else {
          if (this.model.url.indexOf('/aid-predictability') > -1) {
              headerRow.push(yearTrn);
              _.each(keys, function (item) {
                  headerRow.push(item);
              });
              headerRow.push(currencyTrn);
          } else if (this.model.url.indexOf('/ftype') > -1) {
              headerRow.push(yearTrn);
              _.each(keys, function (item) {
                  headerRow.push(item);
              });
              headerRow.push(currencyTrn);
              headerRow.push(typeTrn);
          } else if (this.model.get('chartType') === 'fragmentation') {
              // For AMP-23582: we dont want the name from "summary" because thats the origName and not always the same name than the X axis combo selector.
              var firstColumnName = _.find(self.model.get('heatmap_config').models[0].get('columns'), function (item) {
                  return item.origName === self.model.get('summary')[0];
              }).name;
              var secondColumnName = _.find(self.model.get('heatmap_config').models[0].get('columns'), function (item) {
                  return item.origName === self.model.get('summary')[1];
              }).name;
              headerRow.push(firstColumnName);
              headerRow.push(secondColumnName);
              headerRow.push(amountTrn);
              headerRow.push(percentageTrn);
              headerRow.push(currencyTrn);
              headerRow.push(typeTrn);
          }
          if(self.isRtl) {
              headerRow.reverse();
          }
      }
    //if we are in RTL we reverse each element of the array
      if (self.isRtl) {
          csvTransformed.forEach(function (row) {
              row.reverse();
          });
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
