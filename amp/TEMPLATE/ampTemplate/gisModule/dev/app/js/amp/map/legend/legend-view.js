var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var LegendItem = require('./legend-item-view');
var Template = fs.readFileSync(__dirname + '/legend-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'legend',

  template: _.template(Template),

  events: {
    'click a[href="#toggle-legend-collapse"]': 'toggleLegend'
  },

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.app.data, 'show hide sync', this.render);
  },

  render: function() {
    this.$el.html(this.template());
    var content = this.app.data.getAllVisibleLayers().map(function(layer) {
      return (new LegendItem({ model: layer })).render().el;
    }).value();

    if (!_.isEmpty(content)) {
      this.$el.addClass('expanded');  // always expand when new layers are added
      this.$('.legend-content').html(content);
    }

    return this;
  },

  toggleLegend: function() {
    this.$el.toggleClass('expanded');
  }

});
