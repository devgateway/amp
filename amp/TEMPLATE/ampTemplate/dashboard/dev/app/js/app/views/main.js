var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');

var StateLoadError = require('amp-state/index').StateLoadError;

var Controls = require('./controls');
var ChartsView = require('./charts');
var Charts = require('../models/charts-collection');
var Footer = require('./footer');

var TopsChart = require('../models/tops-chart');
var PredictabilityChart = require('../models/predictability-chart');
var FundingTypeChart = require('../models/ftype-chart');

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
          { app: this.app, url: '/rest/dashboard/aidPredictability' }),
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

  report: function(title, messages) {
    console.warn(title + ':', messages);
    var details = {
      title: title,
      messages: messages,
      id: _.uniqueId('report')
    };
    this.$el.parent().append(modalTemplate({details: details}));
    this.$el.parent().find('#' + details.id).modal();
  }

});
