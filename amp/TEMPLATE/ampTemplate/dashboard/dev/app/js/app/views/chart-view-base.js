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
      get: _.partial(_(this.model.pick).bind(this.model), 'limit', 'adjtype', 'view', 'big'),
      set: _(this.model.set).bind(this.model),
      empty: null
    });

    _.bindAll(this, 'showChart', 'failLoading');
    if (this.getTTContent) { _.bindAll(this, 'getTTContent'); }
    if (this.chartClickHandler) { _.bindAll(this, 'chartClickHandler'); }
  },

  render: function() {
    this.rendered = true;
    var renderOptions = {
      views: this.chartViews,
      model: this.model,
      chart: this.chartEl,
      util: util
    };
    this.$el.html(template(renderOptions));
    this.message = this.$('.dash-chart-diagnostic');
    this.chartContainer = this.$('.dash-chart-wrap');

    if (this.model.get('adjtype') !== void 0) {  // this chart has adj settings
      this.app.settings.load().done(_(function() {
        var adjSettings = this.app.settings.get('0');  // id for Funding Type
        if (!adjSettings) { this.app.report('Could not find Funding Type settings'); }

        this.$('.ftype-options').html(
          _(adjSettings.get('options')).map(function(opt) {
            return adjOptTemplate({
              opt: opt,
              current: (opt.id === this.model.get('adjtype'))
            });
          }, this)
        );
      }).bind(this));
    }

    if (this._stateWait.state() !== 'pending') {
      this.updateData();
    }

    this.app.translator.translateDOM(this.el);
    return this;
  },

  updateData: function() {
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
    var chart = getChart(this.model.get('view'), this.model.get('processed'), this.getChartOptions());
    this.chartContainer.html(chart.el);

    this.renderNumbers();
    var limit = this.model.get('limit');
    if (limit) {
      this.$('.reset')[limit === this.model.defaults.limit ? 'hide' : 'show']();
    }
    this.message.stop().fadeOut(200);
  },

  getChartOptions: function() {
    var co = _(_(this.chartOptions).clone() || {}).defaults({
      trimLabels: !this.model.get('big'),
      getTTContent: this.getTTContent,
      clickHandler: this.chartClickHandler,
      width: this.$('.panel-body').width(),
      height: this.$('.panel-body').height()
    });
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
      this.$('.chart-total').html(util.formatKMB()(this.model.get('total')));
    }
    this.$('.chart-currency').html(this.model.get('currency'));
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
    var downloadView = new DownloadView({
      app: this.app,
      model: this.model,
      chartOptions: _(this.getChartOptions()).omit('height', 'width')
    });
    var specialClass = 'dash-download-modal';
    this.app.modal('Download chart', {
      bodyEl: downloadView.render().el,
      specialClass: specialClass,
      i18nTitle: 'amp.dashboard:download-download-chart'
    });
    
    // Translate modal popup.	
   	app.translator.translateDOM($("." + specialClass));
  }

});
