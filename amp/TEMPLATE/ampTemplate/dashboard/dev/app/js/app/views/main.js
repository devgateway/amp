var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');

var StateLoadError = require('amp-state/index').StateLoadError;

var Controls = require('./controls');
var ChartsView = require('./charts');
var Charts = require('../models/charts-collection');
var Footer = require('./footer');

var TopsChart = require('../models/chart-tops');
var PredictabilityChart = require('../models/chart-aid-predictability');
var FundingTypeChart = require('../models/chart-funding-type');

var template = _.template(fs.readFileSync(
  __dirname + '/../templates/main.html', 'UTF-8'));
var modalTemplate = _.template(fs.readFileSync(
  __dirname + '/../templates/modal.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  initialize: function(options) {
    this.app = options.app;

    // try to load an initial state from the url
    try {
      this.app.state.urlMaybeLoad();
    } catch (e) {
      if (e instanceof StateLoadError) {
        this.app.report('Could not load saved dashboard',
          ['If you are trying to load a shared link, please make sure the entire URL was copied']);
        this.app.url.hash('');  // clear the bad saved-state hash
      } else {
        throw e;
      }
    }

    this.controls = new Controls({ app: this.app });
    this.app.settings.load();  // maybe should go in render or something
                               // but we already do other fetches on init so...

    this.charts = new ChartsView({
      app: this.app,
      collection: new Charts([
        new TopsChart(
          { name: 'Top Donor Agencies' },
          { app: this.app, url: '/rest/dashboard/tops/do' }),
        new TopsChart(
          { name: 'Top Regions' },
          { app: this.app, url: '/rest/dashboard/tops/re' }),
        new TopsChart(
          { name: 'Top Sectors' },
          { app: this.app, url: '/rest/dashboard/tops/ps' }),
        new PredictabilityChart(
          { name: 'Aid Predictability' },
          { app: this.app, url: '/rest/dashboard/aid-predictability' }),
        new FundingTypeChart(
          { name: 'Funding Type' },
          { app: this.app, url: '/rest/dashboard/ftype' })
      ], { app: this.app })
    });

    this.footer = new Footer({ app: this.app });
  },

  render: function() {
    this.$el.html(template());
    this.$('.container').html([
      this.controls.render().el,
      this.charts.render().el,
      this.footer.render().el
    ]);
    return this;
  },

  modal: function(title, options) {
    options = _({
      title: title,
      id: _.uniqueId('modal')
    }).extend(options);
    this.$el.parent().append(modalTemplate({m: options}));
    var thisModal = this.$el.parent().find('#' + options.id);
    if (options.bodyEl) { thisModal.find('.modal-body').html(options.bodyEl); }
    thisModal.modal();
    return thisModal[0];  // the actual DOM element
  }

});
