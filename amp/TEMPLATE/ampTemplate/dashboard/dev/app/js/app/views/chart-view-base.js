var fs = require('fs');
var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var getChart = require('../charts/chart');
var util = require('../../ugly/util');
var DownloadView = require('./download');
var ProjectsListModalView = require('./chart-detail-info-modal');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/chart.html', 'UTF-8'));


var adjOptTemplate = _.template('<option value="<%=opt.id%>" ' +
  '<%= current ? selected="selected" : \'\' %>><%=opt.name%></option>');


module.exports = BackboneDash.View.extend({

  uiDefaults: {
    big: false,
    view: 'bar'
  },

  events: {
    'change .dash-adj-type select': 'changeAdjType',
    'change .dash-xaxis-options select': 'changeXAxis',
    'click .reset': 'resetLimit',
    'click .chart-view': 'changeChartView',
    'click .download': 'download',
    'click .expand': 'big',
    'click .retry': 'render',
    'click .heatmap-switch': 'heatmapSwitchAxis'
  },

  chartViews: [
    'bar',
    'pie',
    'heatmap',
    'table'    
  ],  
  
  initialize: function(options) {
    this.app = options.app;
    this.model.set(this.uiDefaults);
    this.rendered = false;
    this._stateWait = new Deferred();
    this.message = null;
    this.showChartPromise = new Deferred();
    this.renderedPromise = new Deferred();

    if (this.app.savedDashes.length) {
      // a bit sketch....
    	  this.app.state.loadPromise.always(this._stateWait.resolve);
      } else {
      this._stateWait.resolve();
    }

    this.listenTo(this.app.filter, 'apply', this.updateData);
    this.listenTo(this.app.settingsWidget, 'applySettings', this.updateData);
    this.listenTo(this.model, 'change:adjtype', this.render);
    this.listenTo(this.model, 'change:xAxisColumn', this.render);
    this.listenTo(this.model, 'change:limit', this.updateData);
    this.listenTo(this.model, 'change:view', this.render);
    this.listenTo(this.model, 'change:programType', this.render);

    this.app.state.register(this, 'chart:' + this.model.url, {
      get: _.partial(_(this.model.pick).bind(this.model), 'limit', 'adjtype', 'xAxisColumn', 'view', 'big','stacked','showPlannedDisbursements','showActualDisbursements','seriesToExclude', 'xLimit', 'yLimit', 'swapAxes'),
      set: _(this.model.set).bind(this.model),
      empty: null
    });

    _.bindAll(this, 'showChart', 'failLoading','hideExportInPublicView','extractNumberFormatSettings');
    if (this.getTTContent) { _.bindAll(this, 'getTTContent'); }
    if (this.chartClickHandler) { _.bindAll(this, 'chartClickHandler'); }
  },

  render: function() {
	var self = this;
	this.renderedPromise = new Deferred();
    var renderOptions = {
      views: this.chartViews,
      model: this.model,
      chart: this.chartEl,
      util: util
    };
    // We need to be sure all dependencies have been loaded before processing each chart (specially the templates).
    $.when(this._stateWait, this.app.filter.loaded, this.app.translator.promise, this.app.settingsWidget.definitions.loaded, this.app.generalSettings.loaded).done(function() {
    	
    	self.extractNumberFormatSettings();
    	self.$el.html(template(renderOptions));
    	self.hideExportInPublicView();
    	self.message = self.$('.dash-chart-diagnostic');
    	self.chartContainer = self.$('.dash-chart-wrap');
	
	    if (self.model.get('adjtype') !== void 0) {  // this chart has adj settings
	    	self.rendered = true;
	        var adjSettings = self.app.settingsWidget.definitions.getFundingTypeSetting(); 	        	
	        if (!adjSettings) { 
	        	self.app.report('Could not find Funding Type settings'); 
	        } else {
	        	if (self.model.get('adjtype') === 'FAKE') {
	        		self.model.set('adjtype', adjSettings.get('value').defaultId);
	        	}
	        }
	        self.$('.ftype-options').html(
	          _(adjSettings.get('value').options).map(function(opt) {
	            return adjOptTemplate({
	              opt: opt,
	              current: (opt.id === self.model.get('adjtype'))
	            });
	          }, self)
	        );
	      
	    } else {
	    	self.rendered = true;
	    }

      console.log('model=====>', self.model.get('programType'));

      //  if (self.model.get('programType')) {  // this chart has programs type
      //       self.rendered = false;
      //       const pgrOptions = [
      //           {
      //               "id": "prg_level_1",
      //               "name": "National Planning Objectives Level 1"
      //           },
      //           {
      //               "id": "prg_level_2",
      //               "name": "National Planning Objectives Level 2"
      //           },
      //           {
      //               "id": "prg_level_3",
      //               "name": "National Planning Objectives Level 3"
      //           }
      //       ];
      //       self.$('.program-options').html(
      //           pgrOptions.map(function(opt) {
      //               return adjOptTemplate({
      //                   opt: opt,
      //                   current: (opt.id === self.model.get('programType'))
      //               });
      //           }, self)
      //       );

      //   } else {
      //       self.rendered = false;
      //   }
	    
	    // For heatmaps add some extra combos.
        if (self.model.get('chartType') === 'fragmentation') {
            var heatMapConfigs = self.model.get('heatmap_config').models[0];
            var thisHeatMapChart = _.find(heatMapConfigs.get('charts'), function (item) {
                return item.name === self.model.get('name');
            });
            self.$('.xaxis-options').html(
                _(thisHeatMapChart.xColumns).map(function (colId) {
                    var item = _.find(heatMapConfigs.get('columns'), function (item, i) {
                        return item.origName === colId;
                    });
                    var opt = {id: item.origName, name: item.name, selected: false, value: item.origName};
                    return adjOptTemplate({
                        opt: opt,
                        current: (opt.id === self.model.get('xAxisColumn'))
                    });
                }, self)
            );
        }
	
	    if (self._stateWait.state() !== 'pending') {
	    	self.updateData();
	    }

	    self.app.translator.translateDOM(self.$el);
        self.$el.find('[data-toggle="tooltip"]').tooltip();
	    self.renderedPromise.resolve();
    });
    return this;
  },

  updateData: function() {
	if(this.app.rendered !== true) { return; }  
    if (!this.rendered) { return; }  // short-circuit on early filters apply event
    if (this._stateWait.state() === 'pending') {  // short-circuit until we have state
      this.message.html('Loading...').attr('data-i18n', 'amp.dashboard:chart-loading-saved-settings');
      app.translator.translateDOM($('.chart-container'));
      //this.message.fadeIn(100);
      return;
    }

    this.showChartPromise = new Deferred(); // We need to reinitialize this promise.
    this.chartContainer.empty();
    this.message.html('<span data-i18n="amp.dashboard:loading">Loading...</span>').fadeIn(100);

    this.app.translator.getTranslations()
      .done(_(function() {  // defer here to prevent a race with translations loading

    	if (this.model.get('chartType') === 'fragmentation') {
    		// We need this for AMP-25599.
    		this.app.translator.translateDOM(this.el);
    	}

        this.model.fetch({
          type: 'POST',  // TODO: move fetch options to model?
          data: JSON.stringify(this.app.filter.serialize())
        })
        .always(_(function() {  }).bind(this))
        .done(this.showChart)
        .fail(this.failLoading);
      }).bind(this))
      .fail(_(function() {
        this.app.report('Could not load translations', [
          'Refreshing the page may fix the issue.']);
      }).bind(this));

  },

  showChart: function() {
	  this.showNegativeAlert();
	  
    // TODO: why are we triggering twice on load???
    if (!this.model.hasData()) {
      this.message.html('No Data Available').attr('data-i18n','amp.dashboard:chart-no-data-available');
      app.translator.translateDOM($('.chart-container'));
      this.resetNumbers();
      return;
    }    
    var chart = getChart(this.model.get('view'), this.model.get('processed'), this.getChartOptions(), this.model);
    this.chartContainer.html(chart.el);

    if (this.model.get('chartType') !== 'fragmentation') {
    	this.renderNumbers();
    	this.fixTitleWidth();
    }
    
    if (this.model.get('chartType') !== 'fragmentation') {
	    var limit = this.model.get('limit');
	    if (limit) {
	      this.$('.reset')[limit === this.model.defaults.limit ? 'hide' : 'show']();
	    }
    } else {
        if (this.model.get('showResetButton')) {
        	this.$('.reset').show();
        } else {
        	this.$('.reset').hide();
        }
    }
    this.message.stop().fadeOut(200);
    
    this.beautifyLegends(this);
    
    if (this.model.get('view') === 'heatmap') {
    	this.handleHeatmapClicks();
    }
        
    this.showChartPromise.resolve();
  },

    getCellContext: function (e, data) {

        var t = e.target,
            x = t.getAttribute('xid'),
            y = t.getAttribute('yid'),
            labelx = data.x[data.xid.indexOf(parseInt(t.getAttribute('xid'), 10))],
            labely = data.y[data.yid.indexOf(parseInt(t.getAttribute('yid'), 10))];

        if (x == undefined || y == undefined) {
            return null;
        }
        return {
            data: data,
            series: {},
            x: {
                raw: labelx,
                fmt: labelx,
                index: x
            },
            y: {
                raw: labely,
                fmt: labely,
                index: y
            }
        };
    },

  handleHeatmapClicks: function() {
	  var self = this;
	  var cell = this.$(".heatmap-cell");
      if (cell) {
          $(cell).on('click', function(e) {
              var context = self.getCellContext(e, self.model.values);
              if (context) {
                  self.modalView = new ProjectsListModalView({app: app, context: context, model: self.model});
                  self.openInfoWindow((context.x.fmt || context.x.raw) + ' - ' + (context.y.fmt || context.y.raw));
              }
          });
      }
	  var others = this.$(".legend-others");
	  if (others) {
		  $(others).on('click', function(evt) {
			  self.model.set('yLimit', self.model.get('yLimit') + self.model.get('originalYLimit'));
			  self.updateData();
		  });
	  }
  },
  
  showNegativeAlert: function() {
    if(this.model.get('view') === 'pie' && _.find(this.model.get('processed')[0].values, function(item) { return item.y < 0;})) {
      this.$('.negative-values-message').show();
    } else {
      this.$('.negative-values-message').hide();
    }
  },

  getChartOptions: function() {	  
    var co = _(_(this.chartOptions).clone() || {}).defaults({
      trimLabels: !this.model.get('big'),
      getTTContent: this.getTTContent,
      clickHandler: this.chartClickHandler,
      width: this.$('.panel-body').width(),
      height: this.$('.panel-body').height()
      
    });
    if(this.model.get('view') == 'multibar'){
  	  co.stacked = this.model.get('stacked');
  	}
    co.model = this.model;
    return co;
  },

  failLoading: function() {
    this.message.html('Failed to load data <small>' + arguments[2] +
      ' <button type="button" class="retry btn btn-warning btn-sm">' +
      '<span class="glyphicon glyphicon-refresh"></span> Retry</button></small>').show();
    console.error('failed loading chart :(', arguments);
  },

  renderNumbers: function() {
    if (this.model.get('total')) {
    	this.$('.chart-total').html(util.translateLanguage(this.model.get('sumarizedTotal'))); // this shall use the format from the server and translate it in the front end
    }
    var self = this;
   var currencyName = app.settingsWidget.definitions.findCurrencyById(self.model.get('currency')).value;    	
    this.$('.chart-currency').html(currencyName);
  },

  resetNumbers: function() {
    this.$('.chart-total').html('');
    this.$('.chart-currency').html('');
  },
  
  fixTitleWidth: function() {
	  var elementsSpace = 10;
	  var max_lines_on_title = 2;
	  var charsToRemove = 5;
	  var title = this.$(".chart-title h2");
	  var titleWidth = $(title).width();
	  var containerWidth = this.$(".panel-heading").width();
	  var amountWidth = this.$(".big-number").width();
	  if (containerWidth < titleWidth + amountWidth) {
		  $(title).css('width', (containerWidth - amountWidth - elementsSpace) + 'px');
		  while (this.calculateTextLines(title) > max_lines_on_title) {
			  $(title).html($(title).html().substring(0, $(title).html().length - charsToRemove) + '...');
			  $(title).attr('data-title', this.model.get('title'));
			  this.addSimpleTooltip(title);
		  }
	  }
  },
  
  calculateTextLines: function(object) {
	  var lineHeight = 24;
	  var lines = Math.floor($(object).height() / lineHeight);
	  return lines;
  },

  resetLimit: function() {
	  if (this.model.get('chartType') === 'fragmentation') {
		  this.model.set('yLimit', this.model.get('originalYLimit'));
		  this.updateData();
	  } else {
		  this.model.set('limit', this.model.defaults.limit);
	  }
  },

  changeAdjType: function(e) {
    var newType = e.currentTarget.value;
    this.model.set('adjtype', newType);
  },
  
  changeXAxis: function(e) {
	  var newType = e.currentTarget.value;
	  this.model.set('xAxisColumn', newType);
  },  

  changeChartView: function(e) {
    var view = util.data(e.currentTarget, 'view');
    this.model.set('view', view);
    this.hideExportInPublicView();
  },
  hideExportInPublicView: function(){
	  var editableDataExportSetting = this.app.generalSettings.get('hide-editable-export-formats-public-view');
	  if(this.model.get('view') === 'table' && editableDataExportSetting == true && this.app.user.get('logged') == false ){
		  this.$el.find('.download').hide();
	  }else{
		  this.$el.find('.download').show();
	  }  
  },  
  big: function() {
    // toggle big/small charts on large screens
    this.model.set('big', !this.model.get('big'));
  },
  setClear: function(shouldBreak) {
    // layout hints, should only be called by ./charts.js
    this.$el[shouldBreak ? 'addClass' : 'removeClass']('clearfix');
  },

  download: function() {     
	var chartOptions = _(this.getChartOptions()).omit('height', 'width');	
    var downloadView = new DownloadView({
      app: this.app,
      model: this.model,
      chartOptions: chartOptions
    });
    var specialClass = 'dash-download-modal';
    this.app.modal('Download chart', {
      bodyEl: downloadView.render().el,
      specialClass: specialClass,
      i18nTitle: 'amp.dashboard:download-download-chart'
    });
    
    // Translate modal popup.	
   	app.translator.translateDOM($("." + specialClass));
  },
  
  heatmapSwitchAxis: function () {
	  if (this.model.get('swapAxes') === true) {
		  this.model.set('swapAxes', false);
	  } else {
		  this.model.set('swapAxes', true);
	  }  
	  this.updateData();
  },

  //AMP-18630: Here we setup a simple tooltip for each legend element.
  beautifyLegends : function(self) {	  
	  var hasValues = false;
	  var hasProcessed = false;
	  if(self.model !== undefined && self.model.get('values') !== undefined && self.model.get('values').length > 0) {
		  hasValues = true;
	  }
	  if(self.model !== undefined && self.model.get('processed') !== undefined && self.model.get('processed').length > 1) {
		  hasProcessed = true;
	  }
	  
	  // Iterate the list of legend elements in DOM (only for this chart) and set a data element called 'data-title' that
	  // will be then used when a hover event is fired.
	  $(this.$el).find(".nv-series").each(function(i, elem) {
		  // Heatmaps dont need a special reprocessing.
		  if (self.model.get('view') !== 'heatmap') {
			  if(hasValues && !hasProcessed) {
				  // Top charts.
				  if(self.model.get('values')[i] !== undefined) {
					  $(elem).data('data-title', self.model.get('values')[i].name);
				  } else {
					// This the last legend "Others" (doesnt come in the data).
			    	$(elem).data('data-title', app.translator.translateSync("amp.dashboard:chart-FundingType-others", "Others"));
				  }
			  } else if(hasProcessed) {
				  // Aid Predictability charts and Funding Type charts.
				  if(self.model.get('processed')[i] !== undefined) {
					  // The extra check is for FT charts that have more legends (grouped, stacked, etc).
					  $(elem).data('data-title', self.model.get('processed')[i].key);
				  }
			  }
		  }
	    
		  // Now bind NV tooltip mechanism to hover event for each legend.
		  self.addSimpleTooltip(elem);
	  });
  },
  
  addSimpleTooltip: function(object) {
	  if ($(object).data('data-title') || $(object).data('title')) {
		  $(object).hover(function() {
			  var title = $(object).data('data-title') ? $(object).data('data-title') : $(object).data('title');
			  var offset = $(object).offset();
	    	  nv.tooltip.show([offset.left, offset.top], "<div class='panel panel-primary panel-popover'><div class='panel-heading'>" + title + "</div></div>");
		  }, function() {
			  nv.tooltip.cleanup();
		  });
	  }
  },
  extractNumberFormatSettings: function(settings) {
		  var numberFormat = {}; 
	      numberFormat.numberFormat = this.app.generalSettings.get('number-format') || '#,#.#';

		  // If the format pattern doesnt have thousands grouping then ignore 'number-group-separator' param or it will 
		  // be used by JS to group by thousands (ie: in the 'Others' columns).
		  if(numberFormat.numberFormat.indexOf(',') !== -1) {			  		  
			  numberFormat.groupSeparator = this.app.generalSettings.get('number-group-separator') || ',';
		  } else {
			  numberFormat.groupSeparator = '';
		  }
		  			  
		  numberFormat.decimalSeparator = this.app.generalSettings.get('number-decimal-separator') || '.';
		  this.app.generalSettings.numberFormatSettings = numberFormat;		
		  
		  this.app.generalSettings.numberDivider = this.app.generalSettings.get('number-divider');		  
		  if (this.app.generalSettings.numberDivider === 1) {
			  this.app.generalSettings.numberDividerDescription = 'amp.dashboard:chart-tops-inunits';
		  } else if(this.app.generalSettings.numberDivider === 1000) {
			  this.app.generalSettings.numberDividerDescription = 'amp.dashboard:chart-tops-inthousands';
		  } else if(this.app.generalSettings.numberDivider === 1000000) {
			  this.app.generalSettings.numberDividerDescription = 'amp.dashboard:chart-tops-inmillions';
		  }else if(this.app.generalSettings.numberDivider === 1000000000) {
			  this.app.generalSettings.numberDividerDescription = 'amp.dashboard:chart-tops-inbillions';
		  }
	  },

    openInfoWindow: function(title) {
        var specialClass = 'dash-settings-modal';
        this.app.modal(title, {
            specialClass: specialClass,
            bodyEl: this.modalView.render().el
        });
        // Translate modal popup.
        app.translator.translateDOM($("." + specialClass));
    }

});
