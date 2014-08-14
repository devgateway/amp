var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var LegendItem = require('./legend-item-view');
var Template = fs.readFileSync(__dirname + '/legend-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'legend',

  template: _.template(Template),

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.app.data, 'show hide', this.render);
  },

  render: function() {
    this.$el.html(this.template());

    this.$('.legend-content').html(
      this.app.data.getAllVisibleLayers().map(function(layer) {
        return (new LegendItem({ model: layer })).render().el;
      }).value()
    );

    return this;
  },

});
