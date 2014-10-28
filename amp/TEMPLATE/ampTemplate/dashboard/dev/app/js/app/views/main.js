var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var TopsChart = require('../models/tops-chart');
var PredictabilityChart = require('../models/predictability-chart');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/main.html', 'UTF-8'));
var modalTemplate = _.template(fs.readFileSync(
  __dirname + '/../templates/modal.html', 'UTF-8'));


var Filters = require('../views/filters');
var ChartsView = require('../views/charts');
var Charts = require('../models/charts-collection');
var Footer = require('../views/footer');



module.exports = BackboneDash.View.extend({

  initialize: function(options) {
    this.app = options.app;

    this.filters = new Filters({ app: this.app });

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
          { app: this.app, url: '/rest/dashboard/aidPredictability' })
      ], { app: this.app })
    });

    this.footer = new Footer({ app: this.app });
  },

  render: function() {
    this.$el.html(template());
    this.$('.container').html([
      this.filters.render().el,
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
    this.$el.append(modalTemplate({details: details}));
    this.$('#' + details.id).modal();
  }

});
