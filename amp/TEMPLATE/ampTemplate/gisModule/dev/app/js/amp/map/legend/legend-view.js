var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var LegendItem = require('./legend-item-view');
var Template = fs.readFileSync(__dirname + '/legend-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'legend expanded',

  template: _.template(Template),

  events: {
    'click a[href="#toggle-legend-collapse"]': 'toggleLegend'
  },

  initialize: function(options) {
    this.app = options.app;
    this.expanded = false;
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

  toggleLegend: function() {
    this.expanded = !this.expanded;
    if (this.expanded) {
      this.$el.addClass('expanded');
    } else {
      this.$el.removeClass('expanded');
    }
  }

});
