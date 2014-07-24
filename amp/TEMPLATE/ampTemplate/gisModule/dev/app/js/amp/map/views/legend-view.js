var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/map-legend-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'legend',

  template: _.template(Template),

  events: {
    'show.bs.collapse': 'uncollapse',
    'hide.bs.collapse': 'collapse'
  },

  initialize: function() {
    this.collapsed = true;
    this.colors = {
      indicator: null,
      points: []
    };
    this.listenTo(Backbone, 'MAP_INDICATOR_COLORS', this.showMapIndicatorColors);
  },

  render: function() {
    this.$el.html(this.template(_.extend({collapsed: this.collapsed}, this.colors)));
    return this;
  },

  collapse: function() { this.collapsed = true; },
  uncollapse: function() { this.collapsed = false; },

  showMapIndicatorColors: function(newColors) {
    this.colors.indicator = newColors;
    this.render();
  }

});
