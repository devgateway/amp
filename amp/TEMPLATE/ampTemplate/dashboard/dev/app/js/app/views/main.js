var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
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
      collection: new Charts([])
    });
    this.footer = new Footer({ app: this.app });

    this.charts.collection.fetch();
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
