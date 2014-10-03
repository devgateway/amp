var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/main.html', 'UTF-8'));


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
        {
          id: 'top-donors',
          name: 'Top Donors',
          desc: 'highest funding donors',
          total: '123,456,789.00'
        },
        {
          id: 'top-sectors',
          name: 'Top Sectors',
          desc: 'most funded sectors',
          total: '123,456,789.00'
        },
        {
          id: 'top-regions',
          name: 'Top Regions',
          desc: 'most funded regions',
          total: '123,456,789.00'
        },
        {
          id: 'funding-type',
          name: 'Funding Type',
          desc: 'overview of types of funding',
          total: '123,456,789.00'
        },
        {
          id: 'Aid Predictability',
          name: 'Top Donors',
          desc: 'how predictable is the aid?',
          total: '123,456,789.00'
        }
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
  }

});
