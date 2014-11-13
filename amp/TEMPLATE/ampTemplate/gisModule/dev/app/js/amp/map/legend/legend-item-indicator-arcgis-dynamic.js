var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-indicator-arcgis-dynamic.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),
  _loaded: null,
  className: 'legend-indicatorarcgisdynamic',

  render: function() {
    var self = this;

    this.model.getLegend().then(function(data) {
      self.renderLegend(data);
    });

    return this;
  },

  renderLegend: function(legendDefinition) {
    if (legendDefinition.layers.length > 0) {
      var esriLegend = legendDefinition.layers[0].legend;
      this.$el.html(this.template(_.extend({}, this.model.toJSON(), {
        status: 'loaded',
        esriLegend: esriLegend
      })));
    }
  }

});
