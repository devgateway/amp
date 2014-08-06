var _ = require('underscore');
var Backbone = require('backbone');


module.exports = Backbone.Model.extend({

  defaults: {
    data: undefined,
    palatte: undefined
  },

  initialize: function(options) {
    this.display = options.display;
    this.data = options.data;
    this.indicator = options.indicator;
    this.listenTo(this.indicator, 'change:selected', this.triggerShowHide);
  },

  triggerShowHide: function(indicator, show) {
    if (show) {
      this.trigger('show', this);
    } else {
      this.trigger('hide', this);
    }
  }

});
