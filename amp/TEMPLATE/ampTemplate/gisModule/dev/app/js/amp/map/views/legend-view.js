var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/map-legend-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'legend',

  template: _.template(Template),

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.app.display, 'update:layers', this.render);
  },

  render: function() {
    var context = {
      layers: this.app.data.getSelectedLayers()
    };
    this.$el.html(this.template(context));
    return this;
  }

});
