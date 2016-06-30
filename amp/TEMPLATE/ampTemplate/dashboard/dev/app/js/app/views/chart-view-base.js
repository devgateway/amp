var fs = require('fs');
var Deferred = require('jquery').Deferred;
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var getChart = require('../charts/chart');
var util = require('../../ugly/util');
var DownloadView = require('./download');
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
    'click .reset': 'resetLimit',
    'click .chart-view': 'changeChartView',
    'click .download': 'download',
    'click .expand': 'big',
    'click .retry': 'render'    
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

    if (this.app.savedDashes.length) {
      // a bit sketch....
      this.app.state.loadPromise.always(this._stateWait.resolve);
    } else {
      this._stateWait.resolve();
    }

    this.listenTo(this.app.filter, 'apply', this.updateData);
    this.listenTo(this.app.settings, 'change', this.updateData);
    this.listenTo(this.model, 'change:adjtype', this.render);
    this.listenTo(this.model, 'change:limit', this.updateData);
    this.listenTo(this.model, 'change:view', this.render);

    this.app.state.register(this, 'chart:' + this.model.url, {
      get: _.partial(_(this.model.pick).bind(this.model), 'limit', 'adjtype', 'view', 'big','stacked','showPlannedDisbursements','showActualDisbursements','seriesToExclude'),
      set: _(this.model.set).bind(this.model),
      empty: null
    });

    _.bindAll(this, 'showChart', 'failLoading','hideExportInPublicView');
    if (this.getTTContent) { _.bindAll(this, 'getTTContent'); }
    if (this.chartClickHandler) { _.bindAll(this, 'chartClickHandler'); }
  },

  render: function() {    
    var renderOptions = {
      views: this.chartViews,
      model: this.model,
      chart: this.chartEl,
      util: util
    };
    this.$el.html(template(renderOptions));
    this.hideExportInPublicView();
    this.message = this.$('.dash-chart-diagnostic');
    this.chartContainer = this.$('.dash-chart-wrap');

    if (this.model.get('adjtype') !== void 0) {  // this chart has adj settings
    	this.app.settings.load().done(_(function() {
    	this.rendered = true;
        var adjSettings = this.app.settings.get('0');  // id for Funding Type
        if (!adjSettings) { 
        	this.app.report('Could not find Funding Type settings'); 
        } else {
        	if (this.model.get('adjtype') === 'FAKE') {
        		this.model.set('adjtype', adjSettings.get('defaultId'));
        	}
        }
        this.$('.ftype-options').html(
          _(adjSettings.get('options')).map(function(opt) {
            return adjOptTemplate({
              opt: opt,
              current: (opt.id === this.model.get('adjtype'))
            });
          }, this)
        );
      }).bind(this));
    } else {
        this.rendered = true;
    }

    if (this._stateWait.state() !== 'pending') {
      this.updateData();
    }

    this.app.translator.translateDOM(this.el);
    return this;
  },

  updateData: function() {
	if(this.app.rendered !== true) { return; }  
    if (!this.rendered) { return; }  // short-circuit on early filters apply event
    if (this._stateWait.state() === 'pending') {  // short-circuit until we have state
      this.message.html('Loading saved settings...').attr('data-i18n', 'amp.dashboard:chart-loading-saved-settings');
      app.translator.translateDOM($('.chart-container'));
      //this.message.fadeIn(100);
      return;
    }

    this.chartContainer.empty();
    this.message.html('<span data-i18n="amp.dashboard:loading">...</span>').fadeIn(100);

    this.app.translator.getTranslations()
      .done(_(function() {  // defer here to prevent a race with translations loading

        /* TODO: Do we really want to localize this and slow things?*/
        //this.app.translator.translateDOM(this.el);

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
    // TODO: why are we triggering twice on load???
    if (!this.model.hasData()) {
      this.message.html('No Data Available').attr('data-i18n','amp.dashboard:chart-no-data-available');
      app.translator.translateDOM($('.chart-container'));
      this.resetNumbers();
      return;
    }
    var chart = getChart(this.model.get('view'), this.model.get('processed'), this.getChartOptions(), this.model);
    this.chartContainer.html(chart.el);

    if (this.model.get('view') !== 'heatmap') {
    	this.renderNumbers();
    }
    var limit = this.model.get('limit');
    if (limit) {
      this.$('.reset')[limit === this.model.defaults.limit ? 'hide' : 'show']();
    }
    this.message.stop().fadeOut(200);
    
    this.beautifyLegends(this);
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
    var currencyName = _.find(app.settings.get('1').get('options'), function(item) {return item.id === self.model.get('currency')}).value;
    this.$('.chart-currency').html(currencyName);
  },

  resetNumbers: function() {
    this.$('.chart-total').html('');
    this.$('.chart-currency').html('');
  },

  resetLimit: function() {
    this.model.set('limit', this.model.defaults.limit);
  },

  changeAdjType: function(e) {
    var newType = e.currentTarget.value;
    this.model.set('adjtype', newType);
  },

  changeChartView: function(e) {
    var view = util.data(e.currentTarget, 'view');
    this.model.set('view', view);
    this.hideExportInPublicView();
  },
  hideExportInPublicView: function(){
	  var editableDataExportSetting = this.app.settings.get('hide-editable-export-formats-public-view');
	  if(this.model.get('view') === 'table' && editableDataExportSetting && editableDataExportSetting.get('defaultId') == "true" && this.app.user.get('logged') == false ){
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

  //AMP-18630: Here we setup a simple tooltip for each legend element.
  beautifyLegends : function(self) {	  
	  var hasValues = false;
	  var hasProcessed = false;
	  if(self.model != undefined && self.model.get('values') != undefined && self.model.get('values').length > 0) {
		  hasValues = true;
	  }
	  if(self.model != undefined && self.model.get('processed') != undefined && self.model.get('processed').length > 1) {
		  hasProcessed = true;
	  }
	  
	  // Iterate the list of legend elements in DOM (only for this chart) and set a data element called 'data-title' that
	  // will be then used when a hover event is fired.
	  $(this.$el).find(".nv-series").each(function(i, elem) {
		  if(hasValues && !hasProcessed) {
			  // Top charts.
			  if(self.model.get('values')[i] != undefined) {
				  $(elem).data('data-title', self.model.get('values')[i].name);
			  } else {
				// This the last legend "Others" (doesnt come in the data).
		    	$(elem).data('data-title', app.translator.translateSync("amp.dashboard:chart-FundingType-others", "Others"));
			  }
		  } else if(hasProcessed) {
			  // Aid Predictability charts and Funding Type charts.
			  if(self.model.get('processed')[i] != undefined) {
				  // The extra check is for FT charts that have more legends (grouped, stacked, etc).
				  $(elem).data('data-title', self.model.get('processed')[i].key);
			  }
		  }
	    
		  // Now bind NV tooltip mechanism to hover event for each legend.
		  if($(elem).data('data-title') || $(elem).data('title')) {
			  $(elem).hover(function() {
	    		var offset = $(this).offset();	    		
	    		//TODO: Check the generation of heatMapChart.js and see if we can set the 'data' field the same way than other charts.
	    		var title = $(elem).data('data-title') ? $(elem).data('data-title') : $(elem).data('title');
	    		//TODO: Remove hardcoded html and use a template.
	    	    nv.tooltip.show([offset.left, offset.top], "<div class='panel panel-primary panel-popover'><div class='panel-heading'>" + title + "</div></div>");
	    	        
	    	    // TODO: Find a way to trigger the mouseover on the bar.
	    	    // $($(this).closest('svg').find(".nv-groups").find(".nv-bar")[i]).trigger('hover');
	    	   }, function() {
	    		   nv.tooltip.cleanup();
	    	   });
		  }
	  });
  }

});
